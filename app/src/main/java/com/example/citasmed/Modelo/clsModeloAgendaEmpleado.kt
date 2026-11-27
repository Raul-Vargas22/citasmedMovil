package com.example.citasmed.Modelo

import com.example.citasmed.Modelo.Retrofit.apiservice
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import retrofit2.Response

class clsModeloAgendaEmpleado {
    private fun campo(o: JsonObject, k: String): String = (o[k] as? JsonPrimitive)?.content.orEmpty()

    private fun lista(respuesta: Response<JsonElement>): List<JsonObject> {
        if (!respuesta.isSuccessful) error(mensajeError(respuesta))
        val array = respuesta.body() as? JsonArray
            ?: error("No se pudieron leer los datos")
        return array.mapNotNull { it as? JsonObject }
    }

    private fun mensajeError(respuesta: Response<JsonElement>): String {
        val codigo = respuesta.code()
        val mensaje = try {
            val cuerpo = respuesta.errorBody()?.string().orEmpty()
            val json = Json.parseToJsonElement(cuerpo) as? JsonObject
            json?.let { campo(it, "mensaje").ifBlank { campo(it, "error") } }.orEmpty()
        } catch (_: Exception) { "" }
        return mensaje.ifBlank {
            when (codigo) {
                401 -> "Sesión vencida. Inicia sesión de nuevo"
                403 -> "Tu cuenta no tiene permiso para esta operación"
                409 -> "El horario no está disponible"
                else -> "Error del servidor (HTTP $codigo)"
            }
        }
    }

    private fun confirmar(respuesta: Response<JsonElement>) {
        if (!respuesta.isSuccessful) error(mensajeError(respuesta))
        val o = respuesta.body() as? JsonObject ?: error("El servidor no confirmó la operación")
        val exito = (o["exito"] as? JsonPrimitive)?.booleanOrNull == true
        if (!exito) error(campo(o, "mensaje").ifBlank { "No se pudo completar la operación" })
    }

    suspend fun consultarCitas(buscar: String = ""): List<dataCitaEmpleado> =
        lista(apiservice.obtenerCitas(if (buscar.isBlank()) emptyMap() else mapOf("buscar" to buscar.trim()))).mapNotNull { o ->
            val id = campo(o, "id_cita").toIntOrNull() ?: return@mapNotNull null
            dataCitaEmpleado(
                id, campo(o, "id_paciente").toIntOrNull() ?: 0,
                campo(o, "id_medico").toIntOrNull() ?: 0,
                campo(o, "nombre_paciente"), campo(o, "nombre_medico"),
                campo(o, "especialidad"), campo(o, "fecha"),
                campo(o, "hora"), campo(o, "motivo"), campo(o, "estado")
            )
        }

    suspend fun consultarMedicos(): List<dataMedicoEmpleado> =
        lista(apiservice.obtenerMedico()).mapNotNull { o ->
            val id = campo(o, "id_medico").toIntOrNull() ?: return@mapNotNull null
            dataMedicoEmpleado(id, campo(o, "nombre"), campo(o, "apellidos"),
                campo(o, "especialidad"), campo(o, "consultorio"))
        }

    suspend fun consultarHorarios(idMedico: Int): List<dataHorarioEmpleado> =
        lista(apiservice.obtenerHorarios(mapOf("id_medico" to idMedico.toString()))).mapNotNull { o ->
            val id = campo(o, "id_horario").toIntOrNull() ?: return@mapNotNull null
            dataHorarioEmpleado(id, campo(o, "fecha"), campo(o, "hora"))
        }

    suspend fun registrar(idPaciente: Int, idMedico: Int, horario: dataHorarioEmpleado, motivo: String) {
        confirmar(apiservice.registrarCita(mapOf(
            "id_paciente" to idPaciente.toString(), "id_medico" to idMedico.toString(),
            "fecha" to horario.fecha, "hora" to horario.hora,
            "motivo" to motivo
        )))
    }

    suspend fun reprogramar(idCita: Int, horario: dataHorarioEmpleado) {
        confirmar(apiservice.modificarCita(mapOf(
            "id_cita" to idCita.toString(),
            "fecha" to horario.fecha, "hora" to horario.hora
        )))
    }

    suspend fun cancelar(idCita: Int) {
        confirmar(apiservice.cancelarCita(mapOf("id_cita" to idCita.toString())))
    }
}
