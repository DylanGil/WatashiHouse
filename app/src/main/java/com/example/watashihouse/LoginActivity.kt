package com.example.watashihouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var button: Button
    lateinit var emailTxt: EditText
    lateinit var passwordTxt: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        button = findViewById(R.id.loginButton);
        emailTxt = findViewById(R.id.editTextEmail);
        passwordTxt = findViewById(R.id.editTextPassword);

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
                    Log.e("hash", user!!.token.toString())
                    //passwordTxt.text = Editable.Factory.getInstance().newEditable(user!!.token)
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
}