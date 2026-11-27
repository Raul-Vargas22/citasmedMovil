package com.example.citasmed.Modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class dataEspecialidad(
    @SerialName("id_especialidad") val id: Int = 0,
    @SerialName("nombre") val nombre: String = "",
    @SerialName("descripcion") val descripcion: String = "",
    @SerialName("estado") val estado: Int = 0
)