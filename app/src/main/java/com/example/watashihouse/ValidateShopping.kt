package com.example.watashihouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.stripe.android.PaymentConfiguration
import com.stripe.android.googlepaylauncher.GooglePayEnvironment
import com.stripe.android.googlepaylauncher.GooglePayPaymentMethodLauncher

class ValidateShopping : AppCompatActivity() {
    private lateinit var validateShopppingButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_validate_shopping)

        var panierPrice = intent.getIntExtra("panierPrice", 0)
        PaymentConfiguration.init(this, "pk_test_51LCT6xCrEfc8tDFiYDS3VxJ3L6Ko8h9W1YTQNWLUyYFGBB3YKlCfaOUT6AjSmNLGJRKlgQregwvnU9feyzwVbOQz00jmapAknC")

        validateShopppingButton = findViewById(R.id.validateShopppingButton)
        val googlePayLauncher = GooglePayPaymentMethodLauncher(
            activity = this,
            config = GooglePayPaymentMethodLauncher.Config(
                environment = GooglePayEnvironment.Test,
                merchantCountryCode = "FR",
                merchantName = "Watashi House"
            ),
            readyCallback = ::onGooglePayReady,
            resultCallback = ::onGooglePayResult
        )

        validateShopppingButton.setOnClickListener {
            // launch `GooglePayLauncher` to confirm a Payment Intent
            googlePayLauncher.present(
                currencyCode = "EUR",
                amount = panierPrice
            )
        }
    }

    private fun onGooglePayReady(isReady: Boolean) {
        // implemented below
    }

    private fun onGooglePayResult(result: GooglePayPaymentMethodLauncher.Result) {
        when (result) {
            is GooglePayPaymentMethodLauncher.Result.Completed -> {
                // Payment succeeded, show a receipt view
                Toast.makeText(applicationContext, "Completed", Toast.LENGTH_SHORT).show()

            }
            GooglePayPaymentMethodLauncher.Result.Canceled -> {
                // User canceled the operation
                Toast.makeText(applicationContext, "Canceled", Toast.LENGTH_SHORT).show()
            }
            is GooglePayPaymentMethodLauncher.Result.Failed -> {
                // Operation failed; inspect `result.error` for the exception
                Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
