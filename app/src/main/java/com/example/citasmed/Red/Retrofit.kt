package com.example.citasmed.Red

import com.example.citasmed.Interface.apiService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object Retrofit {

    private const val BASE_URL = "http://10.226.139.102/citas/"

    private val jsonConfig = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    val apiservice: apiService by lazy {
        val contentType = "application/json".toMediaType()

        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(jsonConfig.asConverterFactory(contentType))
            .build()
            .create(apiServicei::class.java)
    }
}