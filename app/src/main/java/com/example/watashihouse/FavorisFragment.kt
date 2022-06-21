package com.example.watashihouse

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.watashihouse.databinding.FragmentFavorisBinding
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavorisFragment : Fragment() {

    private var _binding: FragmentFavorisBinding? = null
    private val binding get() = _binding!!
    lateinit var recyclerViewMeuble: RecyclerView
    lateinit var loadingCircle: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFavorisBinding.inflate(inflater, container, false)
        var view = inflater.inflate(R.layout.fragment_favoris, container, false)

        recyclerViewMeuble = view.findViewById(R.id.recyclerViewFavoris) as RecyclerView
        loadingCircle = view.findViewById(R.id.progressBar) as ProgressBar

        primaryFunction()
        return view
    }

    private fun primaryFunction(){
            getUserFavoris()
    }

    private fun getUserFavoris() {
        val listOfMeuble = mutableListOf<Meuble>()

        var localStorage = LocalStorage(context, "jwt")
        val retro = Retro().getRetroClientInstance().create(WatashiApi::class.java)
        retro.getUserProductFromFavoris(localStorage.userId, localStorage.jwtToken).enqueue(object :
            Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.isSuccessful){
                    val items = response.body()?.get("items")?.asJsonArray
                    if(items?.size() == 0){}else{
                        //afficher tout les items des favoris
                        items?.forEach {item ->
                            val monMeuble = item?.asJsonObject
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
                            val avis = 4.5F

                            listOfMeuble += Meuble(id, name, description, img1,img2,img3,img4, avis, price.toString())
                        }

                        recyclerViewMeuble.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = MeubleAdapter(listOfMeuble)
                        }
                    }
                    loadingCircle.visibility = View.INVISIBLE
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                t.message?.let { Log.i("MON PUTAIN DE TAG", it) }
                //Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                loadingCircle.visibility = View.INVISIBLE
                Toast.makeText(context, "Erreur serveur: Redémarrer l'application", Toast.LENGTH_SHORT).show()
            }
        })
    }

}