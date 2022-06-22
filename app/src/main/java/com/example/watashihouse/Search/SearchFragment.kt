package com.example.watashihouse.Search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.watashihouse.R
import com.example.watashihouse.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        var view = inflater.inflate(R.layout.fragment_search, container, false)
        primaryFunction()
        return view
    }

    private fun primaryFunction(){
            binding.searchText.text = "monSearch"



    }

}