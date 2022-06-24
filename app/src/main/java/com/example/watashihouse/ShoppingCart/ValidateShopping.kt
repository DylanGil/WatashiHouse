package com.example.watashihouse.ShoppingCart

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.watashihouse.API.*
import com.example.watashihouse.Utils.LocalStorage
import com.example.watashihouse.R
import com.google.gson.JsonObject
import com.stripe.android.PaymentConfiguration
import com.stripe.android.googlepaylauncher.GooglePayEnvironment
import com.stripe.android.googlepaylauncher.GooglePayLauncher
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ValidateShopping : AppCompatActivity() {
    private lateinit var googlePayButton: RelativeLayout
    private lateinit var cbButton: Button
    private lateinit var paymentSheet: PaymentSheet
    private lateinit var totalNumberText: TextView
    private lateinit var panierMeubleIds: ArrayList<String>
    private var listOfCommandeRequestId = mutableListOf<CommandeRequestId>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_validate_shopping)


        var panierPrice = intent.getIntExtra("panierPrice", 0)
        panierMeubleIds = intent.getStringArrayListExtra("panierMeubleId") as ArrayList<String>

        panierMeubleIds.forEach {id ->
            var commandeRequestId = CommandeRequestId(id.toInt())
            listOfCommandeRequestId.add(commandeRequestId)
        }

        totalNumberText = findViewById(R.id.totalNumberText)
        PaymentConfiguration.init(this, "pk_test_51LCT6xCrEfc8tDFiYDS3VxJ3L6Ko8h9W1YTQNWLUyYFGBB3YKlCfaOUT6AjSmNLGJRKlgQregwvnU9feyzwVbOQz00jmapAknC")


        //googlePay
        googlePayButton = findViewById(R.id.googlePayButton)

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

        googlePayButton.setOnClickListener {
            val localStorage = LocalStorage(applicationContext, "jwt")
            val retro = Retro().getRetroClientInstance().create(WatashiApi::class.java)
            retro.validatePaiement(panierPrice,localStorage.jwtToken).enqueue(object : Callback<PaiementResponse> {
                override fun onResponse(call: Call<PaiementResponse>, response: Response<PaiementResponse>) {
                    val user = response.body()
                    if (user != null) {
                        googlePayLauncher.presentForPaymentIntent(user!!.clientSecret.toString())
                    } else {
                        Toast.makeText(applicationContext, "Erreur", Toast.LENGTH_SHORT).show()
                    }
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
        }

        //Achat CB

        cbButton = findViewById(R.id.cbButton)
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)
        cbButton.setOnClickListener {
            val configuration = PaymentSheet.Configuration("Watashi House")

            val localStorage = LocalStorage(applicationContext, "jwt")
            val retro = Retro().getRetroClientInstance().create(WatashiApi::class.java)
            retro.validatePaiement(panierPrice,localStorage.jwtToken).enqueue(object : Callback<PaiementResponse> {
                override fun onResponse(call: Call<PaiementResponse>, response: Response<PaiementResponse>) {
                    val user = response.body()
                    if (user != null) {
                        paymentSheet.presentWithPaymentIntent(user!!.clientSecret.toString(), configuration)
                    } else {
                        Toast.makeText(applicationContext, "Erreur", Toast.LENGTH_SHORT).show()
                    }
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
        }
        getUserShoppingCart()
    }

    //Google Pay

    private fun onGooglePayReady(isReady: Boolean) {}

    private fun onGooglePayResult(result: GooglePayLauncher.Result) {
        when (result) {
            is GooglePayLauncher.Result.Completed -> {
                addToCommande() //ajoute au commande + supprime du panier
                Toast.makeText(applicationContext, "Merci de votre achat", Toast.LENGTH_SHORT).show()
            }
            GooglePayLauncher.Result.Canceled -> {
                // User canceled the operation
                Toast.makeText(applicationContext, "Achat annuler", Toast.LENGTH_SHORT).show()
            }
            is GooglePayLauncher.Result.Failed -> {
                // Operation failed; inspect `result.error` for the exception
                Toast.makeText(applicationContext, "Echec", Toast.LENGTH_SHORT).show()
            }
        }
    }


    //Achat CB

    private fun onPaymentSheetResult(paymentResult: PaymentSheetResult) {
        when (paymentResult) {
            is PaymentSheetResult.Completed -> {
                addToCommande() //ajoute au commande + supprime du panier
                Toast.makeText(applicationContext, "Merci de votre achat", Toast.LENGTH_SHORT).show()
            }
            is PaymentSheetResult.Canceled -> {
                Toast.makeText(applicationContext, "Achat annuler", Toast.LENGTH_SHORT).show()
            }
            is PaymentSheetResult.Failed -> {
                Toast.makeText(applicationContext, "Echec", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getUserShoppingCart() {
        var localStorage = LocalStorage(applicationContext, "jwt")
        val retro = Retro().getRetroClientInstance().create(WatashiApi::class.java)
        retro.getUserProductFromShoppingCart(localStorage.userId, localStorage.jwtToken).enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.isSuccessful){
                    //prix total du panier
                    var prixTotalPanier = response.body()?.get("price")?.asDouble
                    if(prixTotalPanier == 0.0){}else{
                        prixTotalPanier = prixTotalPanier?.div(100)
                        totalNumberText.text = prixTotalPanier.toString() + "€"
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                t.message?.let { Log.i("MON PUTAIN DE TAG", it) }
                //Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                Toast.makeText(applicationContext, "Erreur serveur: Redémarrer l'application", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addToCommande(){
        val localStorage = LocalStorage(applicationContext, "jwt")
        val retro = Retro().getRetroClientInstance().create(WatashiApi::class.java)
        var request = CommandeRequest()
        request.user = CommandeRequestId(localStorage.userId.toInt())
        request.items = listOfCommandeRequestId
        retro.addToCommande(request, localStorage.jwtToken).enqueue(object :
            Callback<ResponseBody> {
            @SuppressLint("RestrictedApi")
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                deleteAllProductFromShoppingCart(retro, localStorage)
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("Error", t.message.toString())
            }

        })
    }

    private fun deleteAllProductFromShoppingCart(retro: WatashiApi,localStorage: LocalStorage){
        retro.deleteAllProductsFromShoppingCart(localStorage.panierId, localStorage.jwtToken).enqueue(object :
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
}
