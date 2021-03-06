package com.example.watashihouse.API

import com.example.watashihouse.Commande.CommandesResponse
import com.google.gson.JsonArray
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
    fun getAllProductsOrderByNote(): Call<JsonObject>

    @GET("/articles")
    fun getAllProducts(): Call<JsonObject>

    @GET("/articles/souscategorie={sousCatId}?sortBy=note")
    fun getProductFromSousCategorie(
        @Path("sousCatId") sousCatId: String): Call<JsonObject>

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

    @DELETE("/paniers/{panierId}/supprimerArticle")
    fun deleteAllProductsFromShoppingCart(
        @Path("panierId") panierId: String,
        @Header("Authentication") Authentication: String): Call<ResponseBody>

    @DELETE("/favoris/{favorisId}/supprimerArticle")
    fun deleteAllProductsFromFavorite(
        @Path("favorisId") favorisId: String,
        @Header("Authentication") Authentication: String): Call<ResponseBody>

    @POST("/paniers/{panierId}/ajouterArticle={meubleId}")
    fun addToShoppingCart(
        @Path("panierId") panierId: String,
        @Path("meubleId") meubleId: String,
        @Header("Authentication") Authentication: String): Call<ResponseBody>

    @POST("/favoris/{favorisId}/ajouterArticle={meubleId}")
    fun addToFavorite(
        @Path("favorisId") favorisId: String,
        @Path("meubleId") meubleId: String,
        @Header("Authentication") Authentication: String): Call<ResponseBody>

    @DELETE("/paniers/{panierId}/supprimerArticle={meubleId}")
    fun deleteFromShoppingCart(
        @Path("panierId") panierId: String,
        @Path("meubleId") meubleId: String,
        @Header("Authentication") Authentication: String): Call<ResponseBody>

    @DELETE("/favoris/{favorisId}/supprimerArticle={meubleId}")
    fun deleteFromFavoris(
        @Path("favorisId") favorisId: String,
        @Path("meubleId") meubleId: String,
        @Header("Authentication") Authentication: String): Call<ResponseBody>

    @GET("/sous-categories")
    fun getSousCategories(): Call<JsonArray>

    @POST("/commandes")
    fun addToCommande(
        @Body commandeRequest: CommandeRequest,
        @Header("Authentication") Authentication: String): Call<ResponseBody>

    @GET("/commandes/utilisateurs={userId}")
    fun getCommandesFromUser(
        @Path("userId") userId: String,
        @Header("Authentication") Authentication: String): Call<CommandesResponse>

    @GET("/commandes/{commandeId}")
    fun getCommande(
        @Path("commandeId") commandeId: String,
        @Header("Authentication") Authentication: String): Call<JsonObject>
}