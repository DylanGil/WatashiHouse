package com.example.watashihouse

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.material.internal.ContextUtils
import com.stripe.android.PaymentConfiguration
import com.stripe.android.googlepaylauncher.GooglePayEnvironment
import com.stripe.android.googlepaylauncher.GooglePayLauncher
import com.stripe.android.googlepaylauncher.GooglePayPaymentMethodLauncher
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ValidateShopping : AppCompatActivity() {
    private lateinit var validateShopppingButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_validate_shopping)


        var panierPrice = intent.getIntExtra("panierPrice", 0)
        PaymentConfiguration.init(this, "pk_test_51LCT6xCrEfc8tDFiYDS3VxJ3L6Ko8h9W1YTQNWLUyYFGBB3YKlCfaOUT6AjSmNLGJRKlgQregwvnU9feyzwVbOQz00jmapAknC")

        validateShopppingButton = findViewById(R.id.validateShopppingButton)
        val googlePayLauncher = GooglePayLauncher(
            activity = this,
            config = GooglePayLauncher.Config(
                environment = GooglePayEnvironment.Test,
                merchantCountryCode = "FR",
                merchantName = "Watashi House"
            ),
            readyCallback = ::onGooglePayReady,
            resultCallback = ::onGooglePayResult
        )

        validateShopppingButton.setOnClickListener {
            val localStorage = LocalStorage(applicationContext, "jwt")
            val retro = Retro().getRetroClientInstance().create(WatashiApi::class.java)
            retro.validatePaiement(panierPrice,localStorage.jwtToken).enqueue(object : Callback<PaiementResponse> {
                override fun onResponse(call: Call<PaiementResponse>, response: Response<PaiementResponse>) {
                    val user = response.body()
                    if (user != null) {
                        //passwordTxt.text = Editable.Factory.getInstance().newEditable(user!!.token)
                        Toast.makeText(applicationContext, user!!.clientSecret.toString(), Toast.LENGTH_SHORT).show()
                        googlePayLauncher.presentForPaymentIntent(user!!.clientSecret.toString())
                    } else {
                        Toast.makeText(applicationContext, "SHLAG", Toast.LENGTH_SHORT).show()
                    }
                    /*Log.e("hash", user!!.data?.hash.toString())
                    Log.e("email", user!!.data?.email.toString())*/

                }

                override fun onFailure(call: Call<PaiementResponse>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Erreur serveur: Redémarrer l'application",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("Error", t.message.toString())
                }
            })
            // launch `GooglePayLauncher` to confirm a Payment Intent
            //ma requete je lui envoie le token (avec l'id du user)
            //il me retourne le client secret que je fou EN BAS LA
            googlePayLauncher.presentForPaymentIntent("")

            /*googlePayLauncher.present(
                currencyCode = "EUR",
                amount = panierPrice
            )*/
        }
    }

    private fun onGooglePayReady(isReady: Boolean) {
        // implemented below
    }

    private fun onGooglePayResult(result: GooglePayLauncher.Result) {
        when (result) {
            is GooglePayLauncher.Result.Completed -> {
                val localStorage = LocalStorage(applicationContext, "jwt")
                // Payment succeeded, show a receipt view
                Toast.makeText(applicationContext, "Completed", Toast.LENGTH_SHORT).show()
                val retro = Retro().getRetroClientInstance().create(WatashiApi::class.java)
                retro.deleteAllProductsFromShoppingCart(localStorage.userId, localStorage.jwtToken).enqueue(object :
                    Callback<ResponseBody> {
                    @SuppressLint("RestrictedApi")
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        setResult(RESULT_OK)
                        finish()
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("Error", t.message.toString())
                    }

                })
            }
            GooglePayLauncher.Result.Canceled -> {
                // User canceled the operation
                Toast.makeText(applicationContext, "Canceled", Toast.LENGTH_SHORT).show()
            }
            is GooglePayLauncher.Result.Failed -> {
                // Operation failed; inspect `result.error` for the exception
                Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
