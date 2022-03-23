package com.example.watashihouse

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.watashihouse.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        primaryFunction()
        return binding.root
    }

    private fun primaryFunction(){
        binding.buttonTest.setOnClickListener{
            binding.testTextView.text = "qsdqsd"
            Toast.makeText(activity,"monToast",Toast.LENGTH_SHORT).show()
        }

    }

}