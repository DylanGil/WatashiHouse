package com.example.watashihouse.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.watashihouse.*
import com.example.watashihouse.API.Retro
import com.example.watashihouse.API.WatashiApi
import com.example.watashihouse.Meuble.Meuble
import com.example.watashihouse.Meuble.MeubleAdapter
import com.example.watashihouse.databinding.FragmentHomeBinding
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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

        val listOfMeuble = mutableListOf<Meuble>()

        val retro = Retro().getRetroClientInstance().create(WatashiApi::class.java)
        retro.getAllProductsOrderByNote().enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.isSuccessful){

                    val result = response.body()?.get("content")?.asJsonArray

                    for (i in 0..7){
                        val element = result?.get(i)
                        val monMeuble = element?.asJsonObject
                        val id = monMeuble?.get("id").toString()
                        var name = monMeuble?.get("name").toString().drop(1).dropLast(1)
                        var price = monMeuble?.get("price")?.asDouble?.div(100)
                        var description = monMeuble?.get("description").toString().drop(1).dropLast(1)
                        var img1 = monMeuble?.get("image1").toString().drop(1).dropLast(1)
                        var img2 = monMeuble?.get("image2").toString().drop(1).dropLast(1)
                        var img3 = "image3"
                        var img4 = "image4"
                        if(monMeuble?.get("image3").toString() == "null")
                            img3 = "image1"
                        if(monMeuble?.get("image4").toString() == "null")
                            img4 = "image2"
                        img3 = monMeuble?.get(img3).toString().drop(1).dropLast(1)
                        img4 = monMeuble?.get(img4).toString().drop(1).dropLast(1)
                        var avis = -1.0f
                        if(monMeuble?.get("note").toString() != "null")
                            avis = monMeuble?.get("note")?.asFloat!!

                            listOfMeuble += Meuble(id, name, description, img1,img2,img3,img4, avis, price.toString());
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
                Toast.makeText(context, "Erreur serveur: Redémarrer l'application", Toast.LENGTH_SHORT).show()
            }
        })

        return view
    }

}