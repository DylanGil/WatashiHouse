package com.example.watashihouse

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.ContextUtils.getActivity
import com.squareup.picasso.Picasso
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
        var meubleImage1: Boolean

        init {
            meubleTitle = itemView.findViewById(R.id.meubleTitle)
            meubleSummary = itemView.findViewById(R.id.meubleSummary)
            meublePrice = itemView.findViewById(R.id.meublePrice)
            meubleImage = itemView.findViewById(R.id.meubleImage)
            meubleImage1 = true
            ratingBar = itemView.findViewById(R.id.ratingBar)
            addToCart = itemView.findViewById(R.id.addToCartButton)
        }

        fun bind(meuble: Meuble) {
            meubleTitle.text = meuble.title
            meubleSummary.text = meuble.summary
            Picasso.get().load(meuble.image).into(meubleImage)
            //meubleImage.setImageResource(meuble.image)
            ratingBar.rating = meuble.rating
            meublePrice.text = meuble.price + "€"
            addToCart.text = "Ajouter au panier"

            meubleImage.setOnClickListener {
                if(meubleImage1){
                Picasso.get().load("https://static.remove.bg/remove-bg-web/eb1bb48845c5007c3ec8d72ce7972fc8b76733b1/assets/start-1abfb4fe2980eabfbbaaa4365a0692539f7cd2725f324f904565a9a744f8e214.jpg").into(meubleImage)
                meubleImage1 = false
                }
                else{
                    Picasso.get().load(meuble.image).into(meubleImage)
                    meubleImage1 = true
                }
            }

            addToCart.setOnClickListener {
                val localStorage = LocalStorage(itemView.context, "jwt")
                val retro = Retro().getRetroClientInstance().create(WatashiApi::class.java)
                retro.addToShoppingCart(localStorage.userId, meuble.id, localStorage.jwtToken).enqueue(object : Callback<ResponseBody> {
                    @SuppressLint("RestrictedApi")
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        Toast.makeText(itemView.rootView.context, meuble.title + " ajouté au panier", Toast.LENGTH_SHORT).show()
                        val mainActivity = getActivity(itemView.context) as MainActivity
                        mainActivity.updateBadgeCount()
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("Error", t.message.toString())
                        Toast.makeText(itemView.rootView.context, "Erreur: Redémarrer l'application", Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }

    }

}