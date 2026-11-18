package com.example.citasmed.Interface

import kotlinx.serialization.json.JsonObject
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface apiService {

    @FormUrlEncoded
    @POST("login.php")
    suspend fun iniciarSesion(
        @Field("correo") correo: String,
        @Field("contrasena") contrasena: String
    ): Response<JsonObject>
}