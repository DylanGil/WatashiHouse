package com.example.watashihouse

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.clear
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.auth0.android.jwt.JWT
import com.example.watashihouse.databinding.FragmentShoppingCartBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.Collection

class ShoppingCartFragment : Fragment() {

    private var _binding: FragmentShoppingCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var dataStore: DataStore<Preferences>
    lateinit var recyclerViewMeuble: RecyclerView
    private lateinit var deleteShoppingCartButton: Button
    private lateinit var totalNumberText: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentShoppingCartBinding.inflate(inflater, container, false)
        var view = inflater.inflate(R.layout.fragment_shopping_cart, container, false)

        var userId = "0"
        dataStore = context?.createDataStore(name = "jwt")!!
        recyclerViewMeuble = view.findViewById(R.id.recyclerViewShoppingCart) as RecyclerView
        totalNumberText = view.findViewById(R.id.totalNumberText) as TextView
        deleteShoppingCartButton = view.findViewById(R.id.deleteShoppingCartButton)
        deleteShoppingCartButton.setOnClickListener {
            val retro = Retro().getRetroClientInstance().create(WatashiApi::class.java)
            retro.deleteAllProductsFromShoppingCart(userId).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Toast.makeText(context, "Le panier a bien été vidé", Toast.LENGTH_SHORT).show()

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("Error", t.message.toString())
                    Toast.makeText(context, "Erreur: Redémarrer l'application", Toast.LENGTH_SHORT).show()
                }

            })
        }
        lifecycleScope?.launch{
            var jwt = readfromLocalStorage("jwt")?.let { JWT(it) }
            userId = jwt?.getClaim("id")?.asString().toString()
        }

        primaryFunction()
        return view
    }

    private fun primaryFunction(){


        lifecycleScope?.launch{
            //readFromLocalStoragee()
        }

    }

    private suspend fun readfromLocalStorage(key: String): String? {
        val dataStoreKey = preferencesKey<String>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }

    private suspend fun readFromLocalStoragee(){
        val listOfMeuble = mutableListOf<MeubleDeleteButton>()
        var prixTotalPanier = 0.0

        val preferences = dataStore.data.first()
        val preferencesMap = preferences.asMap()
        preferencesMap.forEach{ preference ->
            val values = preference.value as Collection<*>
            var meubleTitre = values.elementAt(0).toString()
            var meubleDescription = values.elementAt(1).toString()
            var meubleImage= values.elementAt(2).toString().toInt()
            var meubleAvis = values.elementAt(3).toString().toFloat()
            var meublePrix = values.elementAt(4).toString()
            var priceString = meublePrix.dropLast(1)
            priceString = priceString.replace(",",".")
            prixTotalPanier += priceString.toDouble()

            /**values.forEach{attribute -> //permet de recup les attributs de mon meuble
                Log.d("unAtt", attribute.toString())
            }
            Log.d(preference.key.name, preference.value.toString())*/
            listOfMeuble += MeubleDeleteButton(meubleTitre, meubleDescription, meubleImage, meubleAvis, meublePrix)
            //Log.d("alors ?", newMeuble.toString())

        }
        totalNumberText.text = prixTotalPanier.toString() + "€"
        recyclerViewMeuble.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = MeubleAdapterDeleteButton(listOfMeuble)
        }

    }
}