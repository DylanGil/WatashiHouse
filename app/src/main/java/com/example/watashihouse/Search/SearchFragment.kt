package com.example.watashihouse.Search

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.watashihouse.API.Retro
import com.example.watashihouse.API.WatashiApi
import com.example.watashihouse.R
import com.example.watashihouse.databinding.FragmentSearchBinding
import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var myButton: Button
    private lateinit var newButton: Button
    private lateinit var linearLayout: LinearLayout
    private lateinit var progressBar: ProgressBar
    lateinit var recyclerViewMeuble: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        var view = inflater.inflate(R.layout.fragment_search, container, false)
        progressBar = view.findViewById(R.id.progressBar)


        val retro = Retro().getRetroClientInstance().create(WatashiApi::class.java)
        retro.getSousCategories().enqueue(object : Callback<JsonArray> {
            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                if(response.isSuccessful){

                    addButton(999,"Rechercher un article")
                    val allSousCat = response.body()
                    allSousCat?.forEach { element ->
                        var sousCat = element.asJsonObject
                        val id = sousCat?.get("id")?.asInt
                        val name = sousCat?.get("name").toString().drop(1).dropLast(1)
                        if (id != null) {
                            addButton(id, name)
                        }
                    }
                }
                progressBar.visibility = View.INVISIBLE
            }

            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                //t.message?.let { Log.i("MON PUTAIN DE TAG", it) }
                //Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                progressBar.visibility = View.INVISIBLE
                Toast.makeText(context, "Erreur serveur: Redémarrer l'application", Toast.LENGTH_SHORT).show()
            }
        })
        return view
    }

    private fun addButton(id: Int, name: String){
        linearLayout = view?.findViewById(R.id.rootLayout)!!
        newButton = Button(context)
        newButton.id = id
        newButton.text = name
        newButton.setOnClickListener {
            val intent = Intent(activity, SearchActivity::class.java)
            intent.putExtra("catId", id)
            startActivity(intent)
        }
        linearLayout.addView(newButton)
    }

}