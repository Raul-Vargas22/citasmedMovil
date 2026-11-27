
package com.example.citasmed.Modelo

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class clsModeloPaciente {

    suspend fun consultarMisCitas(): List<dataCitaPaciente> {

        val respuesta = Retrofit.apiservice.obtenerCitas()

        if (!respuesta.isSuccessful) {
            return emptyList()
        }

        return respuesta.body() ?: emptyList()
    }

    suspend fun cancelarCita(idCita: Int): JsonElement? {

        val datos = mapOf(
            "id_cita" to idCita.toString()
        )

        val respuesta = Retrofit.apiservice.cancelarCita(datos)

        if (!respuesta.isSuccessful) {
            return null
        }

        return respuesta.body()
    }

    suspend fun reprogramarCita(idCita: Int, fecha: String, hora: String): JsonElement? {

        val datos = mapOf("id_cita" to idCita.toString(), "fecha" to fecha, "hora" to hora)

        val respuesta = Retrofit.apiservice.modificarCita(datos)

        if (!respuesta.isSuccessful) {
            val error = respuesta.errorBody()?.string()
            throw Exception("Error HTTP ${respuesta.code()}: $error")
        }

        return respuesta.body()
    }

}
