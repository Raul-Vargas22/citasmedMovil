package com.example.citasmed.Modelo

data class dataPacienteEmpleado(
    val id: Int,
    var nombre: String,
    var telefono: String,
    var apellidos: String = "",
    var correo: String = "",
    var curp: String = "",
    var fechaNacimiento: String = ""
)
