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
    private lateinit var dataStore: DataStore<Preferences>

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
        dataStore = context?.createDataStore(name = "jwt")!!
        initAction()
        return view
    }

    private fun initAction() {
        lifecycleScope.launch {
            var jwt = readfromLocalStorage("jwt")?.let { JWT(it) }
            firstNameTxt.text = jwt?.getClaim("firstname")?.asString().toString()
            lastNameTxt.text = jwt?.getClaim("lastname")?.asString().toString()
            emailTxt.text = jwt?.getClaim("email")?.asString().toString()
        }
    }

    private suspend fun readfromLocalStorage(key: String): String? {
        val dataStoreKey = preferencesKey<String>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }

    private fun readJWTJustForInfo() { //just for info
        lifecycleScope.launch {
            var jwt = readfromLocalStorage("jwt")?.let { JWT(it) }
            var jwtFirstName = jwt?.getClaim("firstname")
            var jwtAllClaims = jwt?.claims
            jwtAllClaims?.forEach{element ->
                element.value.asString()?.let { Log.e(element.key, it) }
            }
            jwtFirstName?.asString()?.let { Log.e("jwtFirstName", it) }
            //Toast.makeText(applicationContext, jwtFirstName?.asString(), Toast.LENGTH_SHORT).show()
        }
    }


}