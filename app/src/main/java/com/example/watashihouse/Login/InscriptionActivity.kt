package com.example.watashihouse.Login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.*
import com.example.watashihouse.API.*
import com.example.watashihouse.Utils.LocalStorage
import com.example.watashihouse.databinding.ActivityInscriptionBinding
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
        var isInscription = true
        val localStorage = LocalStorage(applicationContext, "jwt")
        if(checkIfAlreadyLog(localStorage))
        {
            val retro = Retro().getRetroClientInstance().create(WatashiApi::class.java)
            retro.getUserInfo(localStorage.userId, localStorage.jwtToken).enqueue(object : Callback<UserInfoResponse> {
                override fun onResponse(call: Call<UserInfoResponse>, response: Response<UserInfoResponse>) {
                    val user = response.body()
                    if (user != null) {
                        if(user.gender == "Monsieur") {
                            binding.rbHomme.isChecked = true
                            binding.rbFemme.isChecked = false
                        }else{
                            binding.rbHomme.isChecked = false
                            binding.rbFemme.isChecked = true
                        }
                        binding.etFirstName.text = Editable.Factory.getInstance().newEditable(user.firstname)
                        binding.etLastName.text = Editable.Factory.getInstance().newEditable(user.lastname)
                        binding.etEmail.text = Editable.Factory.getInstance().newEditable(user.email)
                        binding.etPhone.text = Editable.Factory.getInstance().newEditable(user.phone)
                        binding.etAdresse.text = Editable.Factory.getInstance().newEditable(user.address)
                        binding.etCodePostal.text = Editable.Factory.getInstance().newEditable(user.zipCode)
                        binding.etCity.text = Editable.Factory.getInstance().newEditable(user.city)
                        binding.etCountry.text = Editable.Factory.getInstance().newEditable(user.country)
                        binding.inscriptionButton.text = "Modifier"
                        isInscription = false
                    }
                }

                override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {
                    Log.e("Error", t.message.toString())
                }
            })


        }


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
            if(isInscription)
                register()
            else
                editUserInfo()
        }
    }

    private fun editUserInfo() {
        val request = UserEditRequest()
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
        request.typeUser = "client"

        val retro = Retro().getRetroClientInstance().create(WatashiApi::class.java)
        val localStorage = LocalStorage(applicationContext, "jwt")
        retro.editUserInfo(localStorage.userId,request, localStorage.jwtToken).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val user = response.body()
                if (user != null) {
                    user.toString()
                    logOk(false)
                } else {
                    Toast.makeText(applicationContext, "Erreur serveur", Toast.LENGTH_SHORT).show()
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
                    logOk(true)
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

    private fun checkIfAlreadyLog(localStorage: LocalStorage): Boolean {
        val userEmail = localStorage.userEmail
        return userEmail != "null"
    }

    private fun logOk(isInscription: Boolean){
        setResult(RESULT_OK)
        finish()
        if(isInscription)
            Toast.makeText(applicationContext, "Vous êtes inscrits", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(applicationContext, "Mise à jour ok", Toast.LENGTH_SHORT).show()

    }

}