package com.example.citasmed.Modelo

data class dataLogin(
    val id_usuario: Int,
    val nombre: String,
    val rol: String,
    val token: String,
    val exito: Boolean
)
