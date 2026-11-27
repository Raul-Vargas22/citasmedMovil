package com.example.citasmed.Modelo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class dataDoctor(
    @SerialName("id_medico") val id: Int = 0,
    @SerialName("id_usuario") val idUsuario: Int = 0,
    @SerialName("nombre") val nombre: String = "",
    @SerialName("apellidos") val apellidos: String = "",
    @SerialName("correo") val correo: String = "",
    @SerialName("id_especialidad") val idEspecialidad: Int = 0,
    @SerialName("especialidad") val especialidad: String = "",
    @SerialName("cedula_profesional") val cedulaProfesional: String = "",
    @SerialName("telefono") val telefono: String = "",
    @SerialName("consultorio") val consultorio: String = ""
)