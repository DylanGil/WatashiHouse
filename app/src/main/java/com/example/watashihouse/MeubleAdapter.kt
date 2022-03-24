package com.example.watashihouse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MeubleAdapter(var items: List<Meuble>) : RecyclerView.Adapter<MeubleAdapter.MeubleViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeubleViewHolder {
        val itemView =LayoutInflater.from(parent.context).inflate(R.layout.item_class, parent, false)
        return MeubleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MeubleViewHolder, position: Int) {
        val book = items[position]
        holder.bind(book)


    }

    override fun getItemCount() = items.size

    inner class MeubleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var tvTitle: TextView
        var tvSummary: TextView
        var tvAuthor: TextView
        var imageBook: ImageView
        var ratingBar: RatingBar

        init {
            tvTitle = itemView.findViewById(R.id.tvTitle)
            tvSummary = itemView.findViewById(R.id.tvSummary)
            tvAuthor = itemView.findViewById(R.id.tvAuthor)
            imageBook = itemView.findViewById(R.id.imageBook)
            ratingBar = itemView.findViewById(R.id.ratingBar)
        }

        fun bind(book: Meuble){
            tvTitle.text = book.title
            tvAuthor.text = book.author
            tvSummary.text = book.summary
            imageBook.setImageResource(book.image)
            ratingBar.rating = book.rating

        }
    }

}