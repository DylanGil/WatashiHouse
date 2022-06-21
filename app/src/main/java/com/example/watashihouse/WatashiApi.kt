package com.example.watashihouse

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface WatashiApi {
    @POST("utilisateurs/connexion")
    fun login(
        @Body userRequest: UserRequest
    ): Call<UserResponse>

    @GET("/articles?sortBy=note&orderBy=DESC")
    fun getAllProducts(): Call<JsonObject>

    @GET("/paniers/utilisateur={userId}")
    fun getUserProductFromShoppingCart(
        @Path("userId") userId: String,
        @Header("Authentication") Authentication: String): Call<JsonObject>

    @DELETE("/paniers/{userId}/supprimerArticle")
    fun deleteAllProductsFromShoppingCart(
        @Path("userId") userId: String,
        @Header("Authentication") Authentication: String): Call<ResponseBody>

    @POST("/paniers/{userId}/ajouterArticle={meubleId}")
    fun addToShoppingCart(
        @Path("userId") userId: String,
        @Path("meubleId") meubleId: String,
        @Header("Authentication") Authentication: String): Call<ResponseBody>

    @DELETE("/paniers/{userId}/supprimerArticle={meubleId}")
    fun deleteFromShoppingCart(
        @Path("userId") userId: String,
        @Path("meubleId") meubleId: String,
        @Header("Authentication") Authentication: String): Call<ResponseBody>
}