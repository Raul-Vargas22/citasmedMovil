package com.example.citasmed.Modelo

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import retrofit2.Response
import kotlinx.serialization.json.JsonElement

class clsModeloEmpleado {

    private fun campo(datos: JsonObject, nombre: String): String {
        val valor = datos[nombre]
        return if (valor == null || valor is JsonNull) "" else (valor as? JsonPrimitive)?.content.orEmpty()
    }

    suspend fun buscarPacientes(texto: String): List<dataPacienteEmpleado> {
        val respuesta = Retrofit.apiservice.buscarPaciente(mapOf("buscar" to texto.trim()))
        if (!respuesta.isSuccessful) {
            throw IllegalStateException("No se pudo consultar pacientes (HTTP ${respuesta.code()})")
        }
        val lista = respuesta.body() as? JsonArray
            ?: throw IllegalStateException("No se pudieron leer los pacientes")
        return lista.mapNotNull { elemento ->
            val paciente = elemento as? JsonObject ?: return@mapNotNull null
            val id = campo(paciente, "id_paciente").toIntOrNull() ?: return@mapNotNull null
            dataPacienteEmpleado(
                id = id,
                nombre = campo(paciente, "nombre"),
                telefono = campo(paciente, "telefono"),
                apellidos = campo(paciente, "apellidos"),
                correo = campo(paciente, "correo"),
                curp = campo(paciente, "curp"),
                fechaNacimiento = campo(paciente, "fecha_nacimiento")
            )
        }
    }

    suspend fun guardarPaciente(paciente: dataPacienteEmpleado?, datos: Map<String, String>) {
        val respuesta: Response<JsonElement> = if (paciente == null) {
            Retrofit.apiservice.registrarPaciente(datos)
        } else {
            Retrofit.apiservice.actualizarPaciente(datos + mapOf("id_paciente" to paciente.id.toString()))
        }
        if (!respuesta.isSuccessful) {
            val mensaje = respuesta.errorBody()?.string()?.let { cuerpo ->
                try {
                    val error = Json.parseToJsonElement(cuerpo) as? JsonObject
                    error?.let { campo(it, "mensaje").ifBlank { campo(it, "error") } }
                } catch (_: Exception) { null }
            }
            throw IllegalStateException(mensaje ?: "No se pudo guardar (HTTP ${respuesta.code()})")
        }
        val cuerpo = respuesta.body() as? JsonObject
        if ((cuerpo?.get("exito") as? JsonPrimitive)?.booleanOrNull != true) {
            throw IllegalStateException(cuerpo?.let { campo(it, "mensaje") }.orEmpty().ifBlank { "No se pudo guardar el paciente" })
        }
    }
}
