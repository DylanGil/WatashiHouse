package com.example.watashihouse

import androidx.fragment.app.FragmentActivity
import com.stripe.android.googlepaylauncher.GooglePayEnvironment
import com.stripe.android.googlepaylauncher.GooglePayPaymentMethodLauncher

class GooglePay {
    companion object {
        private var googlePayLauncher: GooglePayPaymentMethodLauncher? = null

        fun getInstance(activity: FragmentActivity?): GooglePayPaymentMethodLauncher? {
            if(googlePayLauncher == null) {
                googlePayLauncher = activity?.let {
                    GooglePayPaymentMethodLauncher(
                        activity = it,
                        config = GooglePayPaymentMethodLauncher.Config(
                            environment = GooglePayEnvironment.Test,
                            merchantCountryCode = "FR",
                            merchantName = "Watashi House"
                        ),
                        readyCallback = ::onGooglePayReady,
                        resultCallback = ::onGooglePayResult
                    )
                }!!
            }
            return googlePayLauncher
        }
        private fun onGooglePayReady(isReady: Boolean) {
        }

        private fun onGooglePayResult(
            result: GooglePayPaymentMethodLauncher.Result
        ) {
            when (result) {
                is GooglePayPaymentMethodLauncher.Result.Completed -> {

                    val paymentMethodId = result.paymentMethod.id
                }
                GooglePayPaymentMethodLauncher.Result.Canceled -> {
                    // User canceled the operation
                }
                is GooglePayPaymentMethodLauncher.Result.Failed -> {
                    // Operation failed; inspect `result.error` for the exception
                }
            }
        }
    }
}