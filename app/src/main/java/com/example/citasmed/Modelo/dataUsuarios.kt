package com.example.citasmed.Modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class dataUsuarios(
    @SerialName("id_usuario") val idUsuario: Int = 0,
    @SerialName("nombre") val nombre: String = "",
    @SerialName("apellidos") val apellidos: String = "",
    @SerialName("correo") val correo: String = "",
    @SerialName("rol") val rol: String = "",
    @SerialName("estado") val estado: Int = 0
)