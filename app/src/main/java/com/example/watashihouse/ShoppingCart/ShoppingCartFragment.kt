package com.example.watashihouse.ShoppingCart

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.watashihouse.*
import com.example.watashihouse.API.CommandeRequestId
import com.example.watashihouse.Utils.LocalStorage
import com.example.watashihouse.API.Retro
import com.example.watashihouse.API.WatashiApi
import com.example.watashihouse.Meuble.MeubleAdapterDeleteButton
import com.example.watashihouse.Meuble.MeubleDeleteButton
import com.example.watashihouse.databinding.FragmentShoppingCartBinding
import com.google.android.material.internal.ContextUtils
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShoppingCartFragment : Fragment() {

    private var _binding: FragmentShoppingCartBinding? = null
    private val binding get() = _binding!!
    lateinit var recyclerViewMeuble: RecyclerView
    lateinit var loadingCircle: ProgressBar
    private lateinit var deleteShoppingCartButton: Button
    private lateinit var validateShopppingButton: Button
    private lateinit var totalNumberText: TextView
    private var panierPrice = 0
    private var panierMeubleId = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentShoppingCartBinding.inflate(inflater, container, false)
        var view = inflater.inflate(R.layout.fragment_shopping_cart, container, false)
        val localStorage = LocalStorage(context, "jwt")

        recyclerViewMeuble = view.findViewById(R.id.recyclerViewShoppingCart) as RecyclerView
        loadingCircle = view.findViewById(R.id.progressBar) as ProgressBar
        totalNumberText = view.findViewById(R.id.totalNumberText) as TextView
        deleteShoppingCartButton = view.findViewById(R.id.deleteFavoriteButton)
        deleteShoppingCartButton.setOnClickListener {
            val retro = Retro().getRetroClientInstance().create(WatashiApi::class.java)
            retro.deleteAllProductsFromShoppingCart(localStorage.panierId, localStorage.jwtToken).enqueue(object : Callback<ResponseBody> {
                @SuppressLint("RestrictedApi")
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Toast.makeText(context, "Le panier a bien été vidé", Toast.LENGTH_SHORT).show()
                    refreshFragment()
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("Error", t.message.toString())
                    Toast.makeText(context, "Erreur: Redémarrer l'application", Toast.LENGTH_SHORT).show()
                }

            })
        }
        validateShopppingButton = view.findViewById(R.id.googlePayButton)
        validateShopppingButton.setOnClickListener {
            val intent = Intent(activity, ValidateShopping::class.java)
            intent.putExtra("panierPrice", panierPrice)
            intent.putExtra("panierMeubleId", panierMeubleId)
            startActivityForResult(intent, 1)
        }
        primaryFunction()
        return view
    }

    @SuppressLint("RestrictedApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1){
            if(resultCode == RESULT_OK){ //l'achat est bien passé
                refreshFragment()

            }
        }
    }

    private fun primaryFunction(){
        lifecycleScope?.launch{
            //readFromLocalStoragee()
            getUserShoppingCart()
        }
    }

    @SuppressLint("RestrictedApi")
    fun refreshFragment(){
        fragmentManager?.beginTransaction()?.detach(this)?.commit()
        fragmentManager?.beginTransaction()?.attach(this)?.commit()
        val mainActivity = ContextUtils.getActivity(context) as MainActivity
        mainActivity.resetBadgeCount()
    }

    fun refreshFragment(fragment: ShoppingCartFragment){

        fragmentManager?.beginTransaction()?.detach(fragment)?.commit()
        fragmentManager?.beginTransaction()?.attach(fragment)?.commit()
        //val mainActivity = ContextUtils.getActivity(context) as MainActivity
        //mainActivity.resetBadgeCount()
    }

    private fun getUserShoppingCart() {
        val listOfMeuble = mutableListOf<MeubleDeleteButton>()

        var localStorage = LocalStorage(context, "jwt")
        val retro = Retro().getRetroClientInstance().create(WatashiApi::class.java)
        retro.getUserProductFromShoppingCart(localStorage.userId, localStorage.jwtToken).enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.isSuccessful){
                    panierMeubleId.removeAll(panierMeubleId)
                    //prix total du panier
                    val items = response.body()?.get("items")?.asJsonArray
                    val idPanier = response.body()?.get("id")?.asInt
                    panierPrice = response.body()?.get("price")?.asInt!!
                    var prixTotalPanier = response.body()?.get("price")?.asDouble
                    if(prixTotalPanier == 0.0){}else {
                        prixTotalPanier = prixTotalPanier?.div(100)
                        totalNumberText.text = prixTotalPanier.toString() + "€"

                        //afficher tout les items du panier
                        items?.forEach { item ->
                            val monMeuble = item?.asJsonObject
                            val stock = monMeuble?.get("stock")?.asInt
                            if(stock != 0){
                                val id = monMeuble?.get("id").toString()
                                var name = monMeuble?.get("name").toString().drop(1).dropLast(1)
                                var price = monMeuble?.get("price")?.asDouble?.div(100)
                                var description =
                                    monMeuble?.get("description").toString().drop(1).dropLast(1)
                                var img1 = monMeuble?.get("image1").toString().drop(1).dropLast(1)
                                var img2 = monMeuble?.get("image2").toString().drop(1).dropLast(1)
                                var img3 = "image3"
                                var img4 = "image4"
                                if (monMeuble?.get("image3").toString() == "null")
                                    img3 = "image1"
                                if (monMeuble?.get("image4").toString() == "null")
                                    img4 = "image2"
                                img3 = monMeuble?.get(img3).toString().drop(1).dropLast(1)
                                img4 = monMeuble?.get(img4).toString().drop(1).dropLast(1)
                                val avis = 4.5F

                                panierMeubleId.add(id)
                                listOfMeuble += MeubleDeleteButton(
                                    id,
                                    name,
                                    description,
                                    img1,
                                    img2,
                                    img3,
                                    img4,
                                    avis,
                                    price.toString()
                                );
                            }
                    }

                    recyclerViewMeuble.apply {
                        layoutManager = LinearLayoutManager(this.context)
                        adapter = MeubleAdapterDeleteButton(context,this@ShoppingCartFragment, listOfMeuble)
                    }
                }
                loadingCircle.visibility = View.INVISIBLE
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                t.message?.let { Log.i("MON PUTAIN DE TAG", it) }
                //Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                loadingCircle.visibility = View.INVISIBLE
                Toast.makeText(context, "Erreur serveur: Redémarrer l'application", Toast.LENGTH_SHORT).show()
            }
        })
    }
}