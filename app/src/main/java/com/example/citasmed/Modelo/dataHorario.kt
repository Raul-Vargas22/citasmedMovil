package com.example.citasmed.Modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class dataHorario(
    @SerialName("id_horario") val id: Int = 0,
    @SerialName("id_medico") val idMedico: Int = 0,
    @SerialName("fecha") val fecha: String = "",
    @SerialName("hora") val hora: String = "",
    @SerialName("estado") val estado: Int = 0,
    @SerialName("disponible") val disponible: Boolean = false
)