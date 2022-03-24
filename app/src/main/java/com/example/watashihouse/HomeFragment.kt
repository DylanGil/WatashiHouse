package com.example.watashihouse

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.watashihouse.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    //private var recyclerView = binding.recyclerViewHome as RecyclerView
    lateinit var recyclerViewMeuble: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerViewMeuble = view.findViewById(R.id.recyclerViewHome) as RecyclerView

        val items = listOf(
            Meuble("Rich dad poor dad", "test temp summary", R.drawable.book1, 4.5F, "Dylan GIL AMARO"),
            Meuble("Rich dad poor dad", "test temp summary", R.drawable.book2, 3.5F, "Dylan GIL AMARO"),
            Meuble("Rich dad poor dad", "test temp summary", R.drawable.book3, 2.2F, "Dylan GIL AMARO"),
            Meuble("Rich dad poor dad", "test temp summary", R.drawable.book4, 5F, "Dylan GIL AMARO"),
        )
        recyclerViewMeuble.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = MeubleAdapter(items)
        }

        return binding.root
    }

}