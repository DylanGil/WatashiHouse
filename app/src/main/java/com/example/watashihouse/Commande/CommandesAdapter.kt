package com.example.watashihouse.Commande

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.watashihouse.R
import com.squareup.picasso.Picasso


class CommandesAdapter(context : Context, var items: List<Commande>) : RecyclerView.Adapter<CommandesAdapter.CommandeViewHolder>(){

    val mContext = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommandeViewHolder {
        val itemView =LayoutInflater.from(parent.context).inflate(R.layout.commande_class, parent, false)
        return CommandeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CommandeViewHolder, position: Int) {
        val commande = items[position]
        holder.bind(commande)
    }

    override fun getItemCount() = items.size

    inner class CommandeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView.rootView){

        var commandeRelativeLayout : RelativeLayout
        var commandeNumber: TextView
        var commandeDate: TextView
        var commandePrice: TextView
        var commandeImage: ImageView

        init {
            commandeRelativeLayout = itemView.findViewById(R.id.commandeRelativeLayout)
            commandeNumber = itemView.findViewById(R.id.commandeNumber)
            commandeDate = itemView.findViewById(R.id.commandeDate)
            commandePrice = itemView.findViewById(R.id.commandePrice)
            commandeImage = itemView.findViewById(R.id.commandeImage)
        }

        fun bind(commande: Commande) {
            commandeNumber.text = "N°" + commande.number
            commandeDate.text = commande.date
            Picasso.get().load(commande.image).into(commandeImage)
            commandePrice.text = commande.price + "€"

            commandeImage.setOnClickListener {
                getCommande(commande.id)
            }
            commandeRelativeLayout.setOnClickListener {
                getCommande(commande.id)
            }
        }

        private fun getCommande(id: String){
            val intent = Intent(mContext, CommandeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("commandeId", id)
            mContext.startActivity(intent)
        }

    }

}