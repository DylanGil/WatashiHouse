package com.example.watashihouse

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.auth0.android.jwt.JWT
import com.example.watashihouse.databinding.FragmentLoginBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    lateinit var emailTxt: TextView
    lateinit var firstNameTxt: TextView
    lateinit var lastNameTxt: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        val view = inflater.inflate(R.layout.fragment_login, container, false)
        emailTxt = view.findViewById(R.id.tvEmail) as TextView
        firstNameTxt = view.findViewById(R.id.tvFirstName) as TextView
        lastNameTxt = view.findViewById(R.id.tvLastName) as TextView
        initAction()
        return view
    }

    private fun initAction() {
        lifecycleScope.launch {
            val localStorage = LocalStorage(context, "jwt")
            firstNameTxt.text = localStorage.userFirstName
            lastNameTxt.text = localStorage.userLastName
            emailTxt.text = localStorage.userEmail
        }
    }

}