package com.example.citasmed.Modelo

import kotlinx.serialization.Serializable

@Serializable
data class dataRespuesta(
    val exito: Boolean = false,
    val mensaje: String = "",
    val id_cita: Int? = null,
    val id_especialidad: Int? = null,
    val id_horario: Int? = null,
    val id_medico: Int? = null,
    val id_paciente: Int? = null,
    val id_usuario: Int? = null
)