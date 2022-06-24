package com.example.watashihouse.Commande

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.watashihouse.API.Retro
import com.example.watashihouse.API.WatashiApi
import com.example.watashihouse.R
import com.example.watashihouse.Utils.LocalStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommandesActivity : AppCompatActivity() {
    lateinit var recyclerViewCommande: RecyclerView
    lateinit var loadingCircle: ProgressBar
    lateinit var tvNoCommande: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_commandes)

        loadingCircle = findViewById(R.id.progressBar)
        recyclerViewCommande = findViewById(R.id.recyclerViewCommandes)
        tvNoCommande = findViewById(R.id.tvNoCommande)

        var localStorage = LocalStorage(applicationContext, "jwt")
        val retro = Retro().getRetroClientInstance().create(WatashiApi::class.java)

        val listOfCommande = mutableListOf<Commande>()
        retro.getCommandesFromUser(localStorage.userId,localStorage.jwtToken).enqueue(object : Callback<CommandesResponse> {
            override fun onResponse(call: Call<CommandesResponse>, response: Response<CommandesResponse>) {
                if(response.isSuccessful) {
                    val commandes = response.body()

                    if(commandes == null || commandes.size == 0){
                        tvNoCommande.visibility = View.VISIBLE
                        recyclerViewCommande.visibility = View.INVISIBLE
                    }else{
                        commandes?.forEach {commande ->
                            var id = commande.id.toString()
                            var number = commande.number
                            var date = commande.dateOfPurchase.split("T")[0]
                            var price = commande.totalPrice.toDouble().div(100).toString()
                            var image = commande.items[0].image1
                            listOfCommande.add(Commande(id, number, date, price, image))
                        }

                        recyclerViewCommande.apply {
                            layoutManager = LinearLayoutManager(applicationContext)
                            adapter = CommandesAdapter(applicationContext,listOfCommande)
                        }
                    }
                }else{
                    tvNoCommande.visibility = View.VISIBLE
                    recyclerViewCommande.visibility = View.INVISIBLE
                }
                loadingCircle.visibility = View.INVISIBLE
            }

            override fun onFailure(call: Call<CommandesResponse>, t: Throwable) {
                //t.message?.let { Log.i("MON PUTAIN DE TAG", it) }
                //Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                loadingCircle.visibility = View.INVISIBLE
                tvNoCommande.visibility = View.VISIBLE
                recyclerViewCommande.visibility = View.INVISIBLE
                Toast.makeText(applicationContext, "Erreur serveur: Redémarrer l'application", Toast.LENGTH_SHORT).show()
            }
        })

    }
}