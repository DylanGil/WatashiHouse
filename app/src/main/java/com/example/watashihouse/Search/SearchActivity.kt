package com.example.watashihouse.Search

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.watashihouse.API.Retro
import com.example.watashihouse.API.WatashiApi
import com.example.watashihouse.Meuble.Meuble
import com.example.watashihouse.Meuble.MeubleAdapter
import com.example.watashihouse.R
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    lateinit var recyclerViewSousCat: RecyclerView
    lateinit var meubleAdapter: MeubleAdapter
    lateinit var loadingCircle: ProgressBar
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var imageSearch: ImageView
    lateinit var editSearch: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        var catId = intent.getIntExtra("catId", 0)
        recyclerViewSousCat = findViewById(R.id.recyclerViewSousCatProduct)
        loadingCircle = findViewById(R.id.progressBar)
        toolbar = findViewById(R.id.toolbar2)
        imageSearch = toolbar.findViewById(R.id.imageSearch)
        editSearch = toolbar.findViewById(R.id.editSearch)
        setSupportActionBar(toolbar)

        //Toast.makeText(applicationContext,catId.toString(),Toast.LENGTH_SHORT).show()
        //Toast.makeText(applicationContext,catName,Toast.LENGTH_SHORT).show()
        val listOfMeuble = mutableListOf<Meuble>()

        imageSearch.setOnClickListener {
            val search = editSearch.text.toString()
            meubleAdapter.filter.filter(search)

        }

        val retro = Retro().getRetroClientInstance().create(WatashiApi::class.java)

        if(catId == 999){
            retro.getAllProducts().enqueue(object : Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if(response.isSuccessful){
                        val result = response.body()?.get("content")?.asJsonArray

                        result?.forEach{element ->
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

                        meubleAdapter = MeubleAdapter(listOfMeuble)
                        recyclerViewSousCat.apply {
                            layoutManager = LinearLayoutManager(this.context)
                            adapter = meubleAdapter
                        }
                    }
                    loadingCircle.visibility = View.INVISIBLE

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    //t.message?.let { Log.i("MON PUTAIN DE TAG", it) }
                    //Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                    loadingCircle.visibility = View.INVISIBLE
                    Toast.makeText(applicationContext, "Erreur serveur: Redémarrer l'application", Toast.LENGTH_SHORT).show()
                }
            })

        }else {
            retro.getProductFromSousCategorie(catId.toString()).enqueue(object :
                Callback<JsonObject> {
                @SuppressLint("RestrictedApi")
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                    val content = response.body()?.get("content")?.asJsonArray

                    content?.forEach { item ->
                        val monMeuble = item?.asJsonObject
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

                        listOfMeuble += Meuble(
                            id,
                            name,
                            description,
                            img1,
                            img2,
                            img3,
                            img4,
                            avis,
                            price.toString()
                        );
                    }

                    meubleAdapter = MeubleAdapter(listOfMeuble)
                    recyclerViewSousCat.apply {
                        layoutManager = LinearLayoutManager(this.context)
                        adapter = meubleAdapter
                    }
                    loadingCircle.visibility = View.INVISIBLE
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.e("Error", t.message.toString())
                    loadingCircle.visibility = View.INVISIBLE
                    Toast.makeText(
                        applicationContext,
                        "Erreur: Redémarrer l'application",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        }
    }
}