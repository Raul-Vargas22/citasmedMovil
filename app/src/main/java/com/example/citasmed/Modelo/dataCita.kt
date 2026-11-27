package com.example.citasmed.Modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class dataCita(
    @SerialName("id_cita") val id: Int = 0,
    @SerialName("id_paciente") val idPaciente: Int = 0,
    @SerialName("id_medico") val idMedico: Int = 0,
    @SerialName("fecha") val fecha: String = "",
    @SerialName("hora") val hora: String = "",
    @SerialName("motivo") val motivo: String = "",
    @SerialName("estado") val estado: String = "",
    @SerialName("fecha_registro") val fechaRegistro: String = "",
    @SerialName("nombre_paciente") val nombrePaciente: String = "",
    @SerialName("nombre_medico") val nombreMedico: String = "",
    @SerialName("id_especialidad") val idEspecialidad: Int = 0,
    @SerialName("especialidad") val especialidad: String = ""
)