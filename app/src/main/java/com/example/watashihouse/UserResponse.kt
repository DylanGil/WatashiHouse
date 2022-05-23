package com.example.watashihouse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserResponse {
    /*@SerializedName("data")
    @Expose
    var data: User?= null*/
    @SerializedName("token")
    @Expose
    var token: String?= null

    class User{
        @SerializedName("email")
        @Expose
        var email: String?= null

        @SerializedName("hash")
        @Expose
        var hash: String?= null
    }
}