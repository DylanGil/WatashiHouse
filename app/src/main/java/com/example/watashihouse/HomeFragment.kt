package com.example.watashihouse

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.watashihouse.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    //private var recyclerView = binding.recyclerViewHome as RecyclerView
    lateinit var recyclerViewMeuble: RecyclerView
    lateinit var loadingCircle: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerViewMeuble = view.findViewById(R.id.recyclerViewHome) as RecyclerView
        loadingCircle = view.findViewById(R.id.progressBar) as ProgressBar


        val retrofit = Retrofit.Builder()
            .baseUrl("https://chrome-backbone-347212.ew.r.appspot.com/articles/")
            //.baseUrl("https://api.openweathermap.org/data/2.5/weather/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val weatherService = retrofit.create(WatashiApiLocal::class.java)

        val result = weatherService.urlParams()

        val listOfMeuble = mutableListOf<Meuble>()

        result.enqueue(object : Callback<JsonObject> {

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.isSuccessful){

                    val result = response.body()?.get("content")?.asJsonArray

                    for (i in 1..6){
                        val element = result?.get(i)
                        val monMeuble = element?.asJsonObject
                        val id = monMeuble?.get("id").toString()
                        var name = monMeuble?.get("name").toString()
                        name = name.drop(1)
                        name = name.dropLast(1)
                        val price = monMeuble?.get("price").toString()
                        var truePrice = "0"
                        if (price.length == 4){
                            val firstPrice = price.substring(0, 2)
                            val secondPrice = price.substring(2)
                            truePrice = "$firstPrice,$secondPrice"
                        }else{
                            val firstPrice = price.substring(0, 1)
                            val secondPrice = price.substring(1)
                            truePrice = "$firstPrice,$secondPrice"
                        }
                        var description = monMeuble?.get("description").toString()
                        description = description.drop(1)
                        description = description.dropLast(1)
                        val avis = 4.5F

                        listOfMeuble += Meuble(name, description, R.drawable.book1, avis, truePrice);
                    }

                    recyclerViewMeuble.apply {
                        layoutManager = LinearLayoutManager(this.context)
                        adapter = MeubleAdapter(listOfMeuble)
                    }
                }
                loadingCircle.visibility = View.INVISIBLE

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                //t.message?.let { Log.i("MON PUTAIN DE TAG", it) }
                //Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                loadingCircle.visibility = View.INVISIBLE
                Toast.makeText(context, "Erreur serveur: redémarrer l'application", Toast.LENGTH_SHORT).show()
            }



        })

        return view
    }

}