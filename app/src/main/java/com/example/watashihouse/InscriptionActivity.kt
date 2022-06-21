package com.example.watashihouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.*
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InscriptionActivity : AppCompatActivity() {
    lateinit var inscriptionButton: Button
    lateinit var radioGroup: RadioGroup
    lateinit var radioButton: RadioButton
    lateinit var firstName: EditText
    lateinit var lastName: EditText
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var phoneNumber: EditText
    lateinit var adresse: EditText
    lateinit var codePostal: EditText
    lateinit var city: EditText
    lateinit var country: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inscription)

        inscriptionButton = findViewById(R.id.inscriptionButton)
        radioGroup = findViewById(R.id.rgHommeFemme)
        firstName = findViewById(R.id.etFirstName)
        lastName = findViewById(R.id.etLastName)
        email = findViewById(R.id.etEmail)
        password = findViewById(R.id.etPassword)
        phoneNumber = findViewById(R.id.etPhone)
        adresse = findViewById(R.id.etAdresse)
        codePostal = findViewById(R.id.etCodePostal)
        city = findViewById(R.id.etCity)
        country = findViewById(R.id.etCountry)

        inscriptionButton.setOnClickListener {
            if(TextUtils.isEmpty(firstName.text)) {
                Toast.makeText(this, "Veuillez entrez un prenom", Toast.LENGTH_SHORT).show();
                return@setOnClickListener;
            }
            if(TextUtils.isEmpty(lastName.text)) {
                Toast.makeText(this, "Veuillez entrez un nom de famille", Toast.LENGTH_SHORT).show();
                return@setOnClickListener;
            }
            if(TextUtils.isEmpty(email.text) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email.text).matches()) {
                Toast.makeText(this, "Veuillez entrez un email valide", Toast.LENGTH_SHORT).show();
                return@setOnClickListener;
            }
            if(TextUtils.isEmpty(password.text)) {
                Toast.makeText(this, "Veuillez entrez un mot de passe", Toast.LENGTH_SHORT).show();
                return@setOnClickListener;
            }
            if(TextUtils.isEmpty(phoneNumber.text) || phoneNumber.text.length < 10) {
                Toast.makeText(this, "Veuillez entrez un n° de telephone valide", Toast.LENGTH_SHORT).show();
                return@setOnClickListener;
            }
            if(TextUtils.isEmpty(adresse.text)) {
                Toast.makeText(this, "Veuillez entrez une adresse", Toast.LENGTH_SHORT).show();
                return@setOnClickListener;
            }
            if(TextUtils.isEmpty(codePostal.text)) {
                Toast.makeText(this, "Veuillez entrez un code postal", Toast.LENGTH_SHORT).show();
                return@setOnClickListener;
            }
            if(TextUtils.isEmpty(city.text)) {
                Toast.makeText(this, "Veuillez entrez une ville", Toast.LENGTH_SHORT).show();
                return@setOnClickListener;
            }
            if(TextUtils.isEmpty(country.text)) {
                Toast.makeText(this, "Veuillez entrez un pays", Toast.LENGTH_SHORT).show();
                return@setOnClickListener;
            }
            val selectedId = radioGroup.checkedRadioButtonId
            radioButton = findViewById(selectedId)
            //Toast.makeText(applicationContext, "Inscription", Toast.LENGTH_SHORT).show()
            register()
        }
    }

    private fun register() {
        val request = UserInscriptionRequest()
        request.gender = radioButton.text.toString()
        request.lastname = lastName.text.toString()
        request.firstname = firstName.text.toString()
        request.email = email.text.toString()
        request.hash = password.text.toString()
        request.phone = phoneNumber.text.toString()
        request.address = adresse.text.toString()
        request.zipCode = codePostal.text.toString()
        request.city = city.text.toString()
        request.country = country.text.toString()

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