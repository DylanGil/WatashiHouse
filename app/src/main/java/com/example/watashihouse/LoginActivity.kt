package com.example.watashihouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.auth0.android.jwt.JWT
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.HashMap

class LoginActivity : AppCompatActivity() {
    lateinit var button: Button
    lateinit var emailTxt: EditText
    lateinit var passwordTxt: EditText
    private lateinit var dataStore: DataStore<Preferences>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        button = findViewById(R.id.loginButton);
        emailTxt = findViewById(R.id.editTextEmail);
        passwordTxt = findViewById(R.id.editTextPassword);
        dataStore = createDataStore(name = "jwt")

        emailTxt.text = Editable.Factory.getInstance().newEditable("dga@gmail.com")
        passwordTxt.text = Editable.Factory.getInstance().newEditable("dga")

        button.setOnClickListener {
            login()
        }

        //val actionBar = supportActionBar

        //actionBar!!.title = "Login"

        //actionBar.setDisplayHomeAsUpEnabled(true)
    }

    private fun login() {
        val request = UserRequest()
        request.email = emailTxt.text.toString().trim()
        request.hash = passwordTxt.text.toString().trim()

        val retro = Retro().getRetroClientInstance().create(WatashiApi::class.java)
        retro.login(request).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                val user = response.body()
                if(user != null){
                    Toast.makeText(applicationContext, "Connecté", Toast.LENGTH_SHORT).show()
                    lifecycleScope.launch {
                        clearLocalStorage(dataStore)
                        saveToLocalStorage("jwt", user!!.token.toString())
                        val testJwt = readfromLocalStorage("jwt")
                    }
                    //passwordTxt.text = Editable.Factory.getInstance().newEditable(user!!.token)
                    setResult(RESULT_OK)
                    finish()
                }else{
                    Toast.makeText(applicationContext, "User inexistant", Toast.LENGTH_SHORT).show()
                }
                /*Log.e("hash", user!!.data?.hash.toString())
                Log.e("email", user!!.data?.email.toString())*/

            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "Erreur serveur: Redémarrer l'application", Toast.LENGTH_SHORT).show()
                Log.e("Error", t.message.toString())
            }

        })
    }

    private suspend fun saveToLocalStorage(key: String, value: String){
        val dataStoreKey = preferencesKey<String>(key)
        dataStore.edit { jwt ->
            jwt[dataStoreKey] = value
        }
    }

    private suspend fun readfromLocalStorage(key: String): String? {
        val dataStoreKey = preferencesKey<String>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }

    private suspend fun clearLocalStorage(dataStore: DataStore<Preferences>){
        dataStore.edit {
            it.clear()
        }
    }

    private fun readJWT() {
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