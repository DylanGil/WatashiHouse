package com.example.watashihouse.Meuble

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.watashihouse.Utils.LocalStorage
import com.example.watashihouse.API.Retro
import com.example.watashihouse.API.WatashiApi
import com.example.watashihouse.MainActivity
import com.example.watashihouse.R
import com.example.watashihouse.ShoppingCart.ShoppingCartFragment
import com.google.android.material.internal.ContextUtils
import com.squareup.picasso.Picasso
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MeubleAdapterDeleteButton(context : Context, shoppingCartFragment: ShoppingCartFragment, var items: List<Meuble>) : RecyclerView.Adapter<MeubleAdapterDeleteButton.MeubleViewHolder>() {

    private var mContext = context
    private var shoppingCartFragment = shoppingCartFragment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeubleViewHolder {
        val itemView =LayoutInflater.from(parent.context).inflate(R.layout.item_class_delete_button, parent, false)
        return MeubleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MeubleViewHolder, position: Int) {
        val meuble = items[position]
        holder.bind(meuble)
    }

    override fun getItemCount() = items.size

    inner class MeubleViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView.rootView){

        var meubleTitle: TextView
        var meubleSummary: TextView
        var meublePrice: TextView
        var meubleImage: ImageView
        var ratingBar: RatingBar
        var deleteFromCart: Button
        var wichImg: Int

        init {
            meubleTitle = itemView.findViewById(R.id.commandeNumber)
            meubleSummary = itemView.findViewById(R.id.meubleSummary)
            meublePrice = itemView.findViewById(R.id.commandePrice)
            meubleImage = itemView.findViewById(R.id.commandeImage)
            wichImg = 1
            ratingBar = itemView.findViewById(R.id.ratingBar)
            deleteFromCart = itemView.findViewById(R.id.removeFromCartButton)

        }

        fun bind(meuble: Meuble) {
            meubleTitle.text = meuble.title
            meubleSummary.text = meuble.summary
            Picasso.get().load(meuble.image1).into(meubleImage)
            if(meuble.rating == -1f){
                ratingBar.visibility = View.INVISIBLE
            }
            ratingBar.rating = meuble.rating
            meublePrice.text = meuble.price + "€"
            deleteFromCart.text = "Supprimer du panier"


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

            deleteFromCart.setOnClickListener {
                val localStorage = LocalStorage(itemView.context, "jwt")
                val retro = Retro().getRetroClientInstance().create(WatashiApi::class.java)
                retro.deleteFromShoppingCart(localStorage.panierId, meuble.id, localStorage.jwtToken).enqueue(object :
                    Callback<ResponseBody> {
                    @SuppressLint("RestrictedApi")
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        Toast.makeText(mContext, meuble.title + " supprimé du panier", Toast.LENGTH_SHORT).show()
                        val mainActivity = ContextUtils.getActivity(mContext) as MainActivity
                        mainActivity.removeValueBadgeCount(1)
                        shoppingCartFragment.refreshFragment(shoppingCartFragment)
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