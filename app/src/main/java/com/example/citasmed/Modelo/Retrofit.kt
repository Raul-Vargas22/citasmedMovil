package com.example.citasmed.Modelo

import com.example.citasmed.Interface.apiService
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object Retrofit {

    private const val BASE_URL = "http://10.0.2.2:8080/citas/"

    private val jsonConfig = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    private val cliente = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val token = clsModelo.sesion?.token
            val peticion = chain.request().newBuilder()
            if (!token.isNullOrEmpty()) {
                peticion.header("Authorization", "Bearer $token")
            }
            chain.proceed(peticion.build())
        }
        .build()

    val apiservice: apiService by lazy {
        retrofit2.Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(cliente)
            .addConverterFactory(jsonConfig.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(apiService::class.java)
    }
}
