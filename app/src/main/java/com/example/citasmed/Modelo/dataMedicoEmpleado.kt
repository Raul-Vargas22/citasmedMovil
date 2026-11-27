package com.example.citasmed.Modelo

data class dataMedicoEmpleado(
    val id: Int,
    val nombre: String,
    val apellidos: String,
    val especialidad: String,
    val consultorio: String
) {
    val nombreCompleto: String get() = "$nombre $apellidos".trim()
}

data class dataHorarioEmpleado(
    val id: Int,
    val fecha: String,
    val hora: String
) {
    val etiqueta: String get() = "$fecha · ${hora.take(5)}"
}
