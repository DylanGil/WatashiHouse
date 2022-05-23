package com.example.watashihouse

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface WatashiApiLocal {

    @GET("/articles?sortBy=name")
    fun getAllProducts(): Call<JsonObject>
    //@GET("/user/{id}")
    //suspend fun getUser(@Path("id") id: String): Response<User>666+6+6+
}