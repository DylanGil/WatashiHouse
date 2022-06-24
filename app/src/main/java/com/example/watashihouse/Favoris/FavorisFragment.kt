package com.example.watashihouse.Favoris

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.watashihouse.*
import com.example.watashihouse.Utils.LocalStorage
import com.example.watashihouse.API.Retro
import com.example.watashihouse.API.WatashiApi
import com.example.watashihouse.Meuble.MeubleAdapterDeleteFavoris
import com.example.watashihouse.Meuble.MeubleDeleteFavorite
import com.example.watashihouse.databinding.FragmentFavorisBinding
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavorisFragment : Fragment() {

    private var _binding: FragmentFavorisBinding? = null
    private val binding get() = _binding!!
    lateinit var recyclerViewMeuble: RecyclerView
    lateinit var loadingCircle: ProgressBar
    private lateinit var deleteFavoriteButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFavorisBinding.inflate(inflater, container, false)
        var view = inflater.inflate(R.layout.fragment_favoris, container, false)

        val localStorage = LocalStorage(context, "jwt")
        recyclerViewMeuble = view.findViewById(R.id.recyclerViewFavoris) as RecyclerView
        loadingCircle = view.findViewById(R.id.progressBar) as ProgressBar
        deleteFavoriteButton = view.findViewById(R.id.deleteFavoriteButton)
        deleteFavoriteButton.setOnClickListener {
            val retro = Retro().getRetroClientInstance().create(WatashiApi::class.java)
            retro.deleteAllProductsFromFavorite(localStorage.favorisId, localStorage.jwtToken).enqueue(object : Callback<ResponseBody> {
                @SuppressLint("RestrictedApi")
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Toast.makeText(context, "Les favoris ont bien été vidés", Toast.LENGTH_SHORT).show()
                    refreshFragment(this@FavorisFragment)
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("Error", t.message.toString())
                    Toast.makeText(context, "Erreur: Redémarrer l'application", Toast.LENGTH_SHORT).show()
                }

            })
        }

        primaryFunction()
        return view
    }

    private fun primaryFunction(){
            getUserFavoris()
    }

    fun refreshFragment(fragment: FavorisFragment){

        fragmentManager?.beginTransaction()?.detach(fragment)?.commit()
        fragmentManager?.beginTransaction()?.attach(fragment)?.commit()
        //val mainActivity = ContextUtils.getActivity(context) as MainActivity
        //mainActivity.resetBadgeCount()
    }

    private fun getUserFavoris() {
        val listOfMeuble = mutableListOf<MeubleDeleteFavorite>()

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
                            val stock = monMeuble?.get("stock")?.asInt
                            if(stock != 0) {
                                val id = monMeuble?.get("id").toString()
                                var name = monMeuble?.get("name").toString().drop(1).dropLast(1)
                                var price = monMeuble?.get("price")?.asDouble?.div(100)
                                var description =
                                    monMeuble?.get("description").toString().drop(1).dropLast(1)
                                var img1 = monMeuble?.get("image1").toString().drop(1).dropLast(1)
                                var img2 = monMeuble?.get("image2").toString().drop(1).dropLast(1)
                                var img3 = "image3"
                                var img4 = "image4"
                                if (monMeuble?.get("image3").toString() == "null")
                                    img3 = "image1"
                                if (monMeuble?.get("image4").toString() == "null")
                                    img4 = "image2"
                                img3 = monMeuble?.get(img3).toString().drop(1).dropLast(1)
                                img4 = monMeuble?.get(img4).toString().drop(1).dropLast(1)
                                val avis = 4.5F

                                listOfMeuble += MeubleDeleteFavorite(
                                    id,
                                    name,
                                    description,
                                    img1,
                                    img2,
                                    img3,
                                    img4,
                                    avis,
                                    price.toString()
                                )
                            }
                            }

                        recyclerViewMeuble.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = MeubleAdapterDeleteFavoris(listOfMeuble, this@FavorisFragment)
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