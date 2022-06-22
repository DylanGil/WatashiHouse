package com.example.watashihouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.*
import androidx.lifecycle.lifecycleScope
import com.example.watashihouse.databinding.ActivityInscriptionBinding
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InscriptionActivity : AppCompatActivity() {
    val binding by lazy { ActivityInscriptionBinding.inflate(layoutInflater) }
    lateinit var radioButton: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_inscription)
        setContentView(binding.root)

        binding.inscriptionButton.setOnClickListener {
            if(binding.etFirstName.text.isBlank()) {
                Toast.makeText(this, "Veuillez entrez un prenom", Toast.LENGTH_SHORT).show();
                return@setOnClickListener;
            }
            if(binding.etLastName.text.isBlank()) {
                Toast.makeText(this, "Veuillez entrez un nom de famille", Toast.LENGTH_SHORT).show();
                return@setOnClickListener;
            }
            if(binding.etEmail.text.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text).matches()) {
                Toast.makeText(this, "Veuillez entrez un email valide", Toast.LENGTH_SHORT).show();
                return@setOnClickListener;
            }
            if(binding.etPassword.text.isBlank()) {
                Toast.makeText(this, "Veuillez entrez un mot de passe", Toast.LENGTH_SHORT).show();
                return@setOnClickListener;
            }
            if(binding.etPhone.text.isBlank() || binding.etPhone.text.length < 10) {
                Toast.makeText(this, "Veuillez entrez un n° de telephone valide", Toast.LENGTH_SHORT).show();
                return@setOnClickListener;
            }
            if(binding.etAdresse.text.isBlank()) {
                Toast.makeText(this, "Veuillez entrez une adresse", Toast.LENGTH_SHORT).show();
                return@setOnClickListener;
            }
            if(binding.etCodePostal.text.isBlank()) {
                Toast.makeText(this, "Veuillez entrez un code postal", Toast.LENGTH_SHORT).show();
                return@setOnClickListener;
            }
            if(binding.etCity.text.isBlank()) {
                Toast.makeText(this, "Veuillez entrez une ville", Toast.LENGTH_SHORT).show();
                return@setOnClickListener;
            }
            if(binding.etCountry.text.isBlank()) {
                Toast.makeText(this, "Veuillez entrez un pays", Toast.LENGTH_SHORT).show();
                return@setOnClickListener;
            }
            val selectedId = binding.rgHommeFemme.checkedRadioButtonId
            radioButton = findViewById(selectedId)
            //Toast.makeText(applicationContext, "Inscription", Toast.LENGTH_SHORT).show()
            register()
        }
    }

    private fun register() {
        val request = UserInscriptionRequest()
        request.gender = radioButton.text.toString()
        request.lastname = binding.etLastName.text.toString()
        request.firstname = binding.etFirstName.text.toString()
        request.email = binding.etEmail.text.toString()
        request.hash = binding.etPassword.text.toString()
        request.phone = binding.etPhone.text.toString()
        request.address = binding.etAdresse.text.toString()
        request.zipCode = binding.etCodePostal.text.toString()
        request.city = binding.etCity.text.toString()
        request.country = binding.etCountry.text.toString()

        val retro = Retro().getRetroClientInstance().create(WatashiApi::class.java)
        retro.register(request).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val user = response.body()
                if (user != null) {
                    //passwordTxt.text = Editable.Factory.getInstance().newEditable(user!!.token)
                    logOk()
                } else {
                    Toast.makeText(applicationContext, "User inexistant", Toast.LENGTH_SHORT).show()
                }
                /*Log.e("hash", user!!.data?.hash.toString())
                Log.e("email", user!!.data?.email.toString())*/

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
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
        Toast.makeText(applicationContext, "Vous êtes inscrits", Toast.LENGTH_SHORT).show()
    }

}