package com.example.watashihouse.Meuble

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.watashihouse.Utils.LocalStorage
import com.example.watashihouse.API.Retro
import com.example.watashihouse.API.WatashiApi
import com.example.watashihouse.Favoris.FavorisFragment
import com.example.watashihouse.MainActivity
import com.example.watashihouse.R
import com.google.android.material.internal.ContextUtils.getActivity
import com.squareup.picasso.Picasso
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MeubleAdapterDeleteFavoris(var items: List<Meuble>, favorisFragment: FavorisFragment) : RecyclerView.Adapter<MeubleAdapterDeleteFavoris.MeubleViewHolder>() {


    private var favorisFragment = favorisFragment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeubleViewHolder {
        val itemView =LayoutInflater.from(parent.context).inflate(R.layout.item_class_delete_favoris, parent, false)
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
        var addToFavorite: Button
        var wichImg: Int

        init {
            meubleTitle = itemView.findViewById(R.id.commandeNumber)
            meubleSummary = itemView.findViewById(R.id.meubleSummary)
            meublePrice = itemView.findViewById(R.id.commandePrice)
            meubleImage = itemView.findViewById(R.id.commandeImage)
            wichImg = 1
            ratingBar = itemView.findViewById(R.id.ratingBar)
            addToCart = itemView.findViewById(R.id.addToCartButton)
            addToFavorite = itemView.findViewById(R.id.addToFavoriteButton)
        }

        fun bind(meuble: Meuble) {
            meubleTitle.text = meuble.title
            meubleSummary.text = meuble.summary
            Picasso.get().load(meuble.image1).into(meubleImage)
            if(meuble.rating == -1f){
                ratingBar.visibility = View.INVISIBLE
            }
            ratingBar.rating = meuble.rating
            meublePrice.text = meuble.price + "???"
            addToCart.text = "Ajouter au panier"

            meubleImage.setOnClickListener {
                when(wichImg){
                    1 -> {
                        Picasso.get().load(meuble.image2).into(meubleImage)
                        wichImg = 2
                    }
                    2 -> {
                        Picasso.get().load(meuble.image3).into(meubleImage)
                        wichImg = 3
                    }
                    3 -> {
                        Picasso.get().load(meuble.image4).into(meubleImage)
                        wichImg = 4
                    }
                    4 -> {
                        Picasso.get().load(meuble.image1).into(meubleImage)
                        wichImg = 1
                    }
                }
            }

            addToCart.setOnClickListener {
                val localStorage = LocalStorage(itemView.context, "jwt")
                val retro = Retro().getRetroClientInstance().create(WatashiApi::class.java)
                retro.addToShoppingCart(localStorage.panierId, meuble.id, localStorage.jwtToken).enqueue(object : Callback<ResponseBody> {
                    @SuppressLint("RestrictedApi")
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        Toast.makeText(itemView.rootView.context, meuble.title + " ajout?? au panier", Toast.LENGTH_SHORT).show()
                        val mainActivity = getActivity(itemView.context) as MainActivity
                        mainActivity.addValueBadgeCount(1)

                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("Error", t.message.toString())
                        Toast.makeText(itemView.rootView.context, "Erreur: Red??marrer l'application", Toast.LENGTH_SHORT).show()
                    }

                })
            }

            addToFavorite.setOnClickListener {
                val localStorage = LocalStorage(itemView.context, "jwt")
                val retro = Retro().getRetroClientInstance().create(WatashiApi::class.java)
                retro.deleteFromFavoris(localStorage.favorisId, meuble.id, localStorage.jwtToken).enqueue(object : Callback<ResponseBody> {
                    @SuppressLint("RestrictedApi")
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        Toast.makeText(itemView.rootView.context, meuble.title + " supprim?? des favoris", Toast.LENGTH_SHORT).show()
                        favorisFragment.refreshFragment(favorisFragment)

                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("Error", t.message.toString())
                        Toast.makeText(itemView.rootView.context, "Erreur: Red??marrer l'application", Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }

    }

}