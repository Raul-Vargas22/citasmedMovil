package com.example.citasmed.Interface

import retrofit2.http.GET

interface apiService {
    @GET("login.php")
    suspend fun obtenerCategorias(): List<dataCategorias>
}