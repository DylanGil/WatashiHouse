package com.example.watashihouse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PaiementResponse {
    @SerializedName("clientSecret")
    @Expose
    var clientSecret: String?= null
}