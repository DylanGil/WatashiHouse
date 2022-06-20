package com.example.watashihouse

import android.annotation.SuppressLint
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
import com.example.watashihouse.databinding.FragmentShoppingCartBinding
import com.google.android.material.internal.ContextUtils
import com.google.gson.JsonObject
import com.stripe.android.PaymentConfiguration
import com.stripe.android.googlepaylauncher.GooglePayEnvironment
import com.stripe.android.googlepaylauncher.GooglePayPaymentMethodLauncher
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
        deleteShoppingCartButton = view.findViewById(R.id.deleteShoppingCartButton)
        deleteShoppingCartButton.setOnClickListener {
            val retro = Retro().getRetroClientInstance().create(WatashiApi::class.java)
            retro.deleteAllProductsFromShoppingCart(localStorage.userId, localStorage.jwtToken).enqueue(object : Callback<ResponseBody> {
                @SuppressLint("RestrictedApi")
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Toast.makeText(context, "Le panier a bien été vidé", Toast.LENGTH_SHORT).show()
                    refreshFragment()
                    val mainActivity = ContextUtils.getActivity(context) as MainActivity
                    mainActivity.resetBadgeCount()

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("Error", t.message.toString())
                    Toast.makeText(context, "Erreur: Redémarrer l'application", Toast.LENGTH_SHORT).show()
                }

            })
        }
        validateShopppingButton = view.findViewById(R.id.validateShopppingButton)
        validateShopppingButton.setOnClickListener {
            val intent = Intent(activity, ValidateShopping::class.java)
            intent.putExtra("panierPrice", panierPrice)
            startActivity(intent) //startActivityForResult(intent, 1)
        }
        primaryFunction()
        return view
    }

    private fun primaryFunction(){
        lifecycleScope?.launch{
            //readFromLocalStoragee()
            getUserShoppingCart()
        }
    }

    private fun refreshFragment(){
        fragmentManager?.beginTransaction()?.detach(this)?.commit()
        fragmentManager?.beginTransaction()?.attach(this)?.commit()
    }

    private fun getUserShoppingCart() {
        val listOfMeuble = mutableListOf<MeubleDeleteButton>()

        var localStorage = LocalStorage(context, "jwt")
        val retro = Retro().getRetroClientInstance().create(WatashiApi::class.java)
        retro.getUserProductFromShoppingCart(localStorage.userId, localStorage.jwtToken).enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.isSuccessful){
                    //prix total du panier
                    val items = response.body()?.get("items")?.asJsonArray
                    val idPanier = response.body()?.get("id")?.asInt
                    panierPrice = response.body()?.get("price")?.asInt!!
                    var prixTotalPanier = response.body()?.get("price")?.asString
                    if(prixTotalPanier == "0"){}else{

                    val firstPrice = prixTotalPanier?.substring(0, prixTotalPanier.length-2)
                    val secondPrice = prixTotalPanier?.substring(prixTotalPanier.length-2)
                    totalNumberText.text = "$firstPrice,$secondPrice€"

                    //afficher tout les items du panier
                    items?.forEach {item ->
                        val monMeuble = item?.asJsonObject
                        val id = monMeuble?.get("id").toString()
                        var name = monMeuble?.get("name").toString()
                        name = name.drop(1)
                        name = name.dropLast(1)
                        val price = monMeuble?.get("price").toString()
                        val firstPrice = price?.substring(0, price.length-2)
                        val secondPrice = price?.substring(price.length-2)
                        var truePrice = "$firstPrice,$secondPrice€"
                        var description = monMeuble?.get("description").toString()
                        description = description.drop(1)
                        description = description.dropLast(1)
                        val avis = 4.5F
                        var img1 = monMeuble?.get("image1").toString().drop(1).dropLast(1)

                        listOfMeuble += MeubleDeleteButton(id, name, description, img1, avis, truePrice);
                    }

                    recyclerViewMeuble.apply {
                        layoutManager = LinearLayoutManager(this.context)
                        adapter = MeubleAdapterDeleteButton(listOfMeuble)
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