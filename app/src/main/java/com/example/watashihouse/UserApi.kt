package com.example.watashihouse

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("utilisateurs/connexion")
    fun login(
        @Body userRequest: UserRequest
    ): Call<UserResponse>
}