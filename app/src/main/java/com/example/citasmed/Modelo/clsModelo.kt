package com.example.citasmed.Modelo

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
}
