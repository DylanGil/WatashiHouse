package com.example.watashihouse.API

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserInscriptionRequest {
    @SerializedName("gender")
    @Expose
    var gender: String?= null

    @SerializedName("lastname")
    @Expose
    var lastname: String?= null

    @SerializedName("firstname")
    @Expose
    var firstname: String?= null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("hash")
    @Expose
    var hash: String? = null

    @SerializedName("phone")
    @Expose
    var phone: String? = null

    @SerializedName("address")
    @Expose
    var address: String? = null

    @SerializedName("zipCode")
    @Expose
    var zipCode: String? = null

    @SerializedName("city")
    @Expose
    var city: String? = null

    @SerializedName("country")
    @Expose
    var country: String? = null

    @SerializedName("typeUser")
    @Expose
    var typeUser: String? = "client"

    @SerializedName("debitCards")
    @Expose
    var debitCards = listOf<Any>()

    @SerializedName("orders")
    @Expose
    var orders = listOf<Any>()

    @SerializedName("opinions")
    @Expose
    var opinions = listOf<Any>()
}