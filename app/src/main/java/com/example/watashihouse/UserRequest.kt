package com.example.watashihouse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserRequest {
    @SerializedName("email")
    @Expose
    var email: String?= null

    @SerializedName("hash")
    @Expose
    var hash: String?= null
}