package com.example.citasmed.Modelo

import kotlinx.serialization.Serializable

@Serializable
data class dataCitaPaciente(
    val id_cita: Int,
    val id_medico: Int,
    val nombre_medico: String,
    val especialidad: String,
    val nombre_paciente: String,
    val fecha: String,
    val hora: String,
    val motivo: String,
    val estado: String
)