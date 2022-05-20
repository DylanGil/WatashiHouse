package com.example.watashihouse

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET

interface WatashiApiLocal {

    //@GET("entries")
    //@GET("?q=Paris&appid=60f94176979d0cce3f7fbfc3167eaac1")
    @GET("?sortBy=name")
    fun urlParams(): Call<JsonObject>
}