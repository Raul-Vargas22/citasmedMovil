package com.example.citasmed.Modelo

data class dataCitaEmpleado(
    val id: Int,
    val idPaciente: Int,
    val idMedico: Int,
    val paciente: String,
    val medico: String,
    val especialidad: String,
    val fecha: String,
    val hora: String,
    val motivo: String,
    val estado: String
)
