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

    @GET("/articles?sortBy=name")
    fun getAllProducts(): Call<JsonObject>

    @GET("/paniers/utilisateur={userId}")
    fun getUserProductFromShoppingCart(
        @Path("userId") userId: String): Call<JsonObject>

    @DELETE("/paniers/{userId}/supprimerArticle")
    fun deleteAllProductsFromShoppingCart(
        @Path("userId") userId: String): Call<ResponseBody>

    @GET("/avis/moyenne/article={meubleId}")
    fun getMoyenneAvisProduit(
        @Path("meubleId") meubleId: String): Call<ResponseBody>

    @POST("/paniers/{userId}/ajouterArticle={meubleId}")
    fun addToShoppingCart(
        @Path("userId") userId: String,
        @Path("meubleId") meubleId: String): Call<ResponseBody>



    //@GET("/user/{id}")
    //suspend fun getUser(@Path("id") id: String): Response<User>
}