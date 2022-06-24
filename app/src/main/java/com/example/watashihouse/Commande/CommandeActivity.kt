package com.example.watashihouse.Commande

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.watashihouse.API.Retro
import com.example.watashihouse.API.WatashiApi
import com.example.watashihouse.Meuble.Meuble
import com.example.watashihouse.Meuble.MeubleAdapter
import com.example.watashihouse.Meuble.MeubleAdapterNoButton
import com.example.watashihouse.R
import com.example.watashihouse.Utils.LocalStorage
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommandeActivity : AppCompatActivity() {
    lateinit var recyclerViewMeuble: RecyclerView
    lateinit var loadingCircle: ProgressBar
    lateinit var totalNumberText: TextView
    lateinit var bottomTotalShopping: RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_commande)

        var commandeId = intent.getStringExtra("commandeId")

        recyclerViewMeuble = findViewById(R.id.recyclerViewCommande)
        loadingCircle = findViewById(R.id.progressBar)
        totalNumberText = findViewById(R.id.totalNumberText)
        bottomTotalShopping = findViewById(R.id.bottomTotalShopping)

        val listOfMeuble = mutableListOf<Meuble>()

        var localStorage = LocalStorage(applicationContext, "jwt")
        val retro = Retro().getRetroClientInstance().create(WatashiApi::class.java)
        if (commandeId != null) {
            retro.getCommande(commandeId, localStorage.jwtToken).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if(response.isSuccessful){
                        totalNumberText.text = response.body()?.get("totalPrice")?.asDouble?.div(100).toString() + "€"
                        val result = response.body()?.get("items")?.asJsonArray

                        result?.forEach{ element ->
                            val monMeuble = element?.asJsonObject
                            val stock = monMeuble?.get("stock")?.asInt
                            if(stock != 0){
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

                                listOfMeuble += Meuble(id, name, description, img1,img2,img3,img4, avis, price.toString())
                            }
                        }

                        recyclerViewMeuble.apply {
                            layoutManager = LinearLayoutManager(this.context)
                            adapter = MeubleAdapterNoButton(listOfMeuble)
                        }
                    }
                    loadingCircle.visibility = View.INVISIBLE
                    bottomTotalShopping.visibility =  View.VISIBLE

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    //t.message?.let { Log.i("MON PUTAIN DE TAG", it) }
                    //Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                    loadingCircle.visibility = View.INVISIBLE
                    Toast.makeText(applicationContext, "Erreur serveur: Redémarrer l'application", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}