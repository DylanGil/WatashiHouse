package com.example.watashihouse

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.watashihouse.databinding.FragmentFavorisBinding

class FavorisFragment : Fragment() {

    private var _binding: FragmentFavorisBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFavorisBinding.inflate(inflater, container, false)

        primaryFunction()
        return binding.root
    }

    private fun primaryFunction(){
        binding.favorisText.text = "monFavoris"

    }

}