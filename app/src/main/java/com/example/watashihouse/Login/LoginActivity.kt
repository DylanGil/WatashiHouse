package com.example.watashihouse.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.watashihouse.*
import com.example.watashihouse.Utils.LocalStorage
import com.example.watashihouse.API.Retro
import com.example.watashihouse.API.UserRequest
import com.example.watashihouse.API.UserResponse
import com.example.watashihouse.API.WatashiApi
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var button: Button
    lateinit var emailTxt: EditText
    lateinit var passwordTxt: EditText
    lateinit var clickHereButton: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        button = findViewById(R.id.loginButton);
        emailTxt = findViewById(R.id.editTextEmail);
        passwordTxt = findViewById(R.id.editTextPassword);
        clickHereButton = findViewById(R.id.tvClickHere)

        checkIfAlreadyLog()

        emailTxt.text = Editable.Factory.getInstance().newEditable("dga@gmail.com")
        passwordTxt.text = Editable.Factory.getInstance().newEditable("dga")

        button.setOnClickListener {
            login()
        }

        clickHereButton.setOnClickListener {
            val intent = Intent(this, InscriptionActivity::class.java)
            startActivityForResult(intent, 1)
        }
    }

    private fun checkIfAlreadyLog() {
        val localStorage = LocalStorage(applicationContext, "jwt")
        val userEmail = localStorage.userEmail
        if (userEmail != "null")
            logOk()
    }

    private fun login() {
        val request = UserRequest()
        request.email = emailTxt.text.toString().trim()
        request.hash = passwordTxt.text.toString().trim()

        val retro = Retro().getRetroClientInstance().create(WatashiApi::class.java)
        retro.login(request).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                val user = response.body()
                if (user != null) {
                    lifecycleScope.launch {
                        val localStorage = LocalStorage(applicationContext, "jwt")
                        localStorage.clearLocalStorage()
                        localStorage.saveToLocalStorage("jwt", user!!.token.toString())
                    }
                    //passwordTxt.text = Editable.Factory.getInstance().newEditable(user!!.token)
                    logOk()
                } else {
                    Toast.makeText(applicationContext, "User inexistant", Toast.LENGTH_SHORT).show()
                }
                /*Log.e("hash", user!!.data?.hash.toString())
                Log.e("email", user!!.data?.email.toString())*/

            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Erreur serveur: Redémarrer l'application",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("Error", t.message.toString())
            }
        })


    }

    private fun logOk(){
        setResult(RESULT_OK)
        finish()
        Toast.makeText(applicationContext, "Connecté", Toast.LENGTH_SHORT).show()
    }

}