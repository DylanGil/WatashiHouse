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
import com.example.watashihouse.MainActivity
import com.example.watashihouse.R
import com.google.android.material.internal.ContextUtils.getActivity
import com.squareup.picasso.Picasso
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MeubleAdapterNoButton(var items: List<Meuble>) : RecyclerView.Adapter<MeubleAdapterNoButton.MeubleViewHolder>(), Filterable {

    var meubleFilteredList: List<Meuble> = ArrayList()

    init {
        meubleFilteredList = items
    }
    override fun getFilter(): Filter {
        return object: Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var charSearch = constraint.toString()
                if(charSearch.isEmpty()){
                    meubleFilteredList = items
                }else{
                    meubleFilteredList = items.filter { meuble ->  meuble.title.lowercase().contains(charSearch.lowercase()) }
                }
                val filterResult = FilterResults()
                filterResult.values = meubleFilteredList
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                meubleFilteredList = results?.values as ArrayList<Meuble>
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeubleViewHolder {
        val itemView =LayoutInflater.from(parent.context).inflate(R.layout.item_class_no_button, parent, false)
        return MeubleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MeubleViewHolder, position: Int) {
        val meuble = meubleFilteredList[position]
        holder.bind(meuble)
    }

    override fun getItemCount() = meubleFilteredList.size

    inner class MeubleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView.rootView){

        var meubleTitle: TextView
        var meubleSummary: TextView
        var meublePrice: TextView
        var meubleImage: ImageView
        var ratingBar: RatingBar
        var wichImg: Int

        init {
            meubleTitle = itemView.findViewById(R.id.commandeNumber)
            meubleSummary = itemView.findViewById(R.id.meubleSummary)
            meublePrice = itemView.findViewById(R.id.commandePrice)
            meubleImage = itemView.findViewById(R.id.commandeImage)
            wichImg = 1
            ratingBar = itemView.findViewById(R.id.ratingBar)
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
        }

    }

}