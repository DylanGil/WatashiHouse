package com.example.watashihouse

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.auth0.android.jwt.JWT
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class MeubleAdapter(var items: List<Meuble>) : RecyclerView.Adapter<MeubleAdapter.MeubleViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeubleViewHolder {
        val itemView =LayoutInflater.from(parent.context).inflate(R.layout.item_class, parent, false)
        return MeubleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MeubleViewHolder, position: Int) {
        val meuble = items[position]
        holder.bind(meuble)
    }

    override fun getItemCount() = items.size

    inner class MeubleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView.rootView){

        var meubleTitle: TextView
        var meubleSummary: TextView
        var meublePrice: TextView
        var meubleImage: ImageView
        var ratingBar: RatingBar
        var addToCart: Button

        init {
            meubleTitle = itemView.findViewById(R.id.meubleTitle)
            meubleSummary = itemView.findViewById(R.id.meubleSummary)
            meublePrice = itemView.findViewById(R.id.meublePrice)
            meubleImage = itemView.findViewById(R.id.meubleImage)
            ratingBar = itemView.findViewById(R.id.ratingBar)
            addToCart = itemView.findViewById(R.id.addToCartButton)
        }

        fun bind(meuble: Meuble) {
            meubleTitle.text = meuble.title
            meubleSummary.text = meuble.summary
            meubleImage.setImageResource(meuble.image)
            ratingBar.rating = meuble.rating
            meublePrice.text = meuble.price + "€"
            addToCart.text = "Ajouter au panier"

            addToCart.setOnClickListener {
                val localStorage = LocalStorage(itemView.context, "jwt")
                val retro = Retro().getRetroClientInstance().create(WatashiApi::class.java)
                retro.addToShoppingCart(localStorage.userId, meuble.id).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        Toast.makeText(itemView.rootView.context, meuble.title + " ajouté au panier", Toast.LENGTH_SHORT).show()

                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("Error", t.message.toString())
                        Toast.makeText(itemView.rootView.context, "Erreur: Redémarrer l'application", Toast.LENGTH_SHORT).show()
                    }

                })
                //val monSet = setOf(meuble.title,meuble.summary, meuble.image.toString(), meuble.rating.toString(),  meuble.price + "€")

                //TODO a faire mtn


            }
        }

    }

}