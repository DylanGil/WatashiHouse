package com.example.watashihouse

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface WatashiApi {
    @POST("utilisateurs/connexion")
    fun login(
        @Body userRequest: UserRequest
    ): Call<UserResponse>

    @GET("/articles?sortBy=name")
    fun getAllProducts(): Call<JsonObject>

    //@GET("/user/{id}")
    //suspend fun getUser(@Path("id") id: String): Response<User>
}