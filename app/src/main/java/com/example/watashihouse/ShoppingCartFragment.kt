package com.example.watashihouse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.clear
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.createDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.watashihouse.databinding.FragmentShoppingCartBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.collections.Collection

class ShoppingCartFragment : Fragment() {

    private var _binding: FragmentShoppingCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var dataStore: DataStore<Preferences>
    lateinit var recyclerViewMeuble: RecyclerView
    private lateinit var deleteLocalStorageButton: Button
    private lateinit var totalNumberText: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentShoppingCartBinding.inflate(inflater, container, false)
        var view = inflater.inflate(R.layout.fragment_shopping_cart, container, false)
        recyclerViewMeuble = view.findViewById(R.id.recyclerViewShoppingCart) as RecyclerView
        totalNumberText = view.findViewById(R.id.totalNumberText) as TextView
        deleteLocalStorageButton = view.findViewById(R.id.deleteLocalStorageButton)
        deleteLocalStorageButton.setOnClickListener {
            Toast.makeText(context, "Le panier a bien été vidé", Toast.LENGTH_SHORT).show()
            lifecycleScope?.launch{
                clearLocalStorage(dataStore)
            }
        }

        primaryFunction()
        return view
    }

    private fun primaryFunction(){
        dataStore = context?.createDataStore(name = "meubleStored")!!

        lifecycleScope?.launch{
            readFromLocalStorage()

        }

    }

    private suspend fun readFromLocalStorage(){
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

    private suspend fun clearLocalStorage(dataStore: DataStore<Preferences>){
        dataStore.edit {
            it.clear()
        }
    }


}