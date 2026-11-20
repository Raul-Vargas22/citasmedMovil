package com.example.citasmed.Presentador

import com.example.citasmed.Modelo.clsModeloEmpleado
import com.example.citasmed.Modelo.dataPacienteEmpleado

class clsPresentadorEmpleado {

    private val modelo = clsModeloEmpleado()

    fun totalPacientes(): Int {
        return modelo.totalPacientes()
    }

    fun buscarPacientes(texto: String): List<dataPacienteEmpleado> {
        return modelo.buscarPacientes(texto)
    }

    fun guardarPaciente(id: Int?, nombre: String, telefono: String): String? {
        val nombreLimpio = nombre.trim()
        val telefonoLimpio = telefono.trim()

        if (nombreLimpio.length < 3) {
            return "Escribe un nombre válido"
        }

        if (telefonoLimpio.length != 10 || !telefonoLimpio.all { it.isDigit() }) {
            return "El teléfono debe tener 10 dígitos"
        }

        if (id == null) {
            modelo.registrarPaciente(nombreLimpio, telefonoLimpio)
        } else {
            modelo.actualizarPaciente(id, nombreLimpio, telefonoLimpio)
        }

        return null
    }
}
