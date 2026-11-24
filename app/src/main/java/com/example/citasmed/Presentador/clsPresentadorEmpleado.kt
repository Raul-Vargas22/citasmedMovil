package com.example.citasmed.Presentador

import android.util.Patterns
import com.example.citasmed.Modelo.clsModeloEmpleado
import com.example.citasmed.Modelo.dataPacienteEmpleado

class clsPresentadorEmpleado {
    private val modelo = clsModeloEmpleado()

    suspend fun buscarPacientes(texto: String): List<dataPacienteEmpleado> =
        modelo.buscarPacientes(texto)

    suspend fun guardarPaciente(
        paciente: dataPacienteEmpleado?, nombre: String, apellidos: String,
        correo: String, telefono: String, curp: String, fechaNacimiento: String
    ): String? {
        if (nombre.trim().length < 2 || apellidos.trim().length < 2) {
            return "Escribe nombre y apellidos"
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches()) return "Correo inválido"
        if (telefono.length != 10 || !telefono.all { it.isDigit() }) return "El teléfono debe tener 10 dígitos"
        if (curp.isNotBlank() && !Regex("[A-Za-z0-9]{18}").matches(curp.trim())) return "CURP inválida"
        if (fechaNacimiento.isNotBlank() && !Regex("""\d{4}-\d{2}-\d{2}""").matches(fechaNacimiento.trim())) {
            return "Fecha: usa AAAA-MM-DD"
        }
        return try {
            modelo.guardarPaciente(paciente, mapOf(
                "nombre" to nombre.trim(),
                "apellidos" to apellidos.trim(),
                "correo" to correo.trim(),
                "telefono" to telefono.trim(),
                "curp" to curp.trim().uppercase(),
                "fecha_nacimiento" to fechaNacimiento.trim()
            ))
            null
        } catch (e: Exception) {
            e.message ?: "No se pudo guardar"
        }
    }
}
