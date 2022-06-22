package com.example.watashihouse.API

import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface WatashiApi {
    @POST("utilisateurs/connexion")
    fun login(
        @Body userRequest: UserRequest
    ): Call<UserResponse>

    @POST("utilisateurs/inscription")
    fun register(
        @Body userInscriptionRequest: UserInscriptionRequest
    ): Call<ResponseBody>

    @GET("/utilisateurs/{userId}")
    fun getUserInfo(
        @Path("userId") userId: String,
        @Header("Authentication") Authentication: String): Call<UserInfoResponse>

    @PUT("/utilisateurs/{userId}")
    fun editUserInfo(
        @Path("userId") userId: String,
        @Body userEditRequest: UserEditRequest,
        @Header("Authentication") Authentication: String): Call<ResponseBody>

    @GET("/articles?sortBy=note&orderBy=DESC")
    fun getAllProducts(): Call<JsonObject>

    @POST("/create-payment-intent/prix={price}")
    fun validatePaiement(
        @Path("price") price: Int,
        @Header("Authentication") Authentication: String): Call<PaiementResponse>

    @GET("/paniers/utilisateur={userId}")
    fun getUserProductFromShoppingCart(
        @Path("userId") userId: String,
        @Header("Authentication") Authentication: String): Call<JsonObject>

    @GET("/favoris/utilisateur={userId}")
    fun getUserProductFromFavoris(
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