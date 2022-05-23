package com.example.watashihouse

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.core.preferencesSetKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
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

    inner class MeubleViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView.rootView){

        var meubleTitle: TextView
        var meubleSummary: TextView
        var meublePrice: TextView
        var meubleImage: ImageView
        var ratingBar: RatingBar
        var addToCart: Button
        private lateinit var dataStore: DataStore<Preferences>

        init {
            meubleTitle = itemView.findViewById(R.id.meubleTitle)
            meubleSummary = itemView.findViewById(R.id.meubleSummary)
            meublePrice = itemView.findViewById(R.id.meublePrice)
            meubleImage = itemView.findViewById(R.id.meubleImage)
            ratingBar = itemView.findViewById(R.id.ratingBar)
            addToCart = itemView.findViewById(R.id.addToCartButton)
            dataStore = itemView.context.createDataStore(name = "meubleStored")

        }

        private suspend fun saveToLocalStorage(key: String, value: Set<String>){
            val dataStoreKey = preferencesSetKey<String>(key)
            dataStore.edit { meubleStored ->
                //Log.d("abc", meubleStored.toString())
               meubleStored[dataStoreKey] = value
            }
        }

        fun bind(meuble: Meuble) {
            meubleTitle.text = meuble.title
            meubleSummary.text = meuble.summary
            meubleImage.setImageResource(meuble.image)
            ratingBar.rating = meuble.rating
            meublePrice.text = meuble.price + "€"
            addToCart.text = "Ajouter au panier"

            addToCart.setOnClickListener {
                Toast.makeText(itemView.context, meuble.title + " ajouté au panier", Toast.LENGTH_SHORT).show()
                itemView.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                    val monSet = setOf(meuble.title,meuble.summary, meuble.image.toString(), meuble.rating.toString(),  meuble.price + "€")
                    saveToLocalStorage(meuble.title, monSet)

                }
            }
        }

    }

}