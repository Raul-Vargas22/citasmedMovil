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
        correo: String, telefono: String, curp: String, fechaNacimiento: String, contrasena: String = ""
    ): String? {
        if (paciente == null && (contrasena.length < 8 || contrasena.length > 72)) return "La contraseña debe tener entre 8 y 72 caracteres"
        if (nombre.trim().length < 2 || apellidos.trim().length < 2) {
            return "Escribe nombre y apellidos"
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches()) return "Revisa el correo electrónico"
        if (telefono.length != 10 || !telefono.all { it.isDigit() }) return "El teléfono debe tener 10 dígitos"
        if (curp.isNotBlank() && !Regex("[A-Za-z0-9]{18}").matches(curp.trim())) return "Revisa la CURP"
        if (fechaNacimiento.isNotBlank()) {
            val fecha = try { java.time.LocalDate.parse(fechaNacimiento.trim()) }
                        catch (_: Exception) { return "Escribe una fecha válida (AAAA-MM-DD)" }
            if (fecha.isAfter(java.time.LocalDate.now())) return "La fecha de nacimiento no puede ser futura"
        }
        return try {
            modelo.guardarPaciente(paciente, mapOf(
                "nombre" to nombre.trim(),
                "apellidos" to apellidos.trim(),
                "correo" to correo.trim(),
                "telefono" to telefono.trim(),
                "curp" to curp.trim().uppercase(),
                "fecha_nacimiento" to fechaNacimiento.trim(),
                "contrasena" to (if (paciente == null) contrasena else "")
            ))
            null
        } catch (e: Exception) {
            e.message ?: "No se pudo guardar"
        }
    }
}
