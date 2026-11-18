package com.example.citasmed.Modelo

data class dataLogin(
    val id_usuario: Int,
    val nombre: String,
    val rol: String,
    val csrf_token: String,
    val exito: Boolean
)