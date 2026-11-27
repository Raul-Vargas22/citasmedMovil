package com.example.citasmed.Modelo

import kotlinx.serialization.json.JsonElement
import retrofit2.Response
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.int
import kotlinx.serialization.json.booleanOrNull

class clsModelo {

    companion object {
        var sesion: dataLogin? = null
    }

    suspend fun validarInicioSesion(correo: String, contra: String): dataLogin? {
        val respuesta = Retrofit.apiservice.iniciarSesion(correo, contra)
        if (!respuesta.isSuccessful) return null
        val datos = respuesta.body() ?: return null
        if (datos["exito"]?.jsonPrimitive?.booleanOrNull != true) return null
        return dataLogin(
            datos.getValue("id_usuario").jsonPrimitive.int,
            datos.getValue("nombre").jsonPrimitive.content,
            datos.getValue("rol").jsonPrimitive.content,
            datos.getValue("token").jsonPrimitive.content,
            true
        )
    }

    suspend fun consultarEspecialidades(): Response<JsonElement> {
        return Retrofit.apiservice.obtenerEspecialidades()
    }

    suspend fun consultarMedicos(idEspecialidad: String): Response<JsonElement> {
        val parametros = mapOf("id_especialidad" to idEspecialidad)
        return Retrofit.apiservice.obtenerMedico(parametros)
    }

    suspend fun consultarHorarios(idMedico: String, fecha: String): Response<JsonElement> {
        val parametros = mapOf(
            "id_medico" to idMedico,
            "fecha" to fecha
        )
        return Retrofit.apiservice.obtenerHorarios(parametros)
    }

    suspend fun registrarCita(idMedico: String, fecha: String, hora: String, motivo: String): Response<JsonElement> {
        val datos = mapOf("id_medico" to idMedico, "fecha" to fecha, "hora" to hora, "motivo" to motivo)
        return Retrofit.apiservice.registrarCita(datos)
    }

}
