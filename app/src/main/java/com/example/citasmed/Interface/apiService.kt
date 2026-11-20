package com.example.citasmed.Interface

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface apiService {

    @FormUrlEncoded
    @POST("login.php")
    suspend fun iniciarSesion(
        @Field("correo") correo: String,
        @Field("contrasena") contrasena: String
    ): Response<JsonObject>

    @GET("buscar_cita.php")
    suspend fun buscarCita(
        @QueryMap parametros: Map<String, String> = emptyMap()
    ): Response<JsonElement>

    @GET("buscar_paciente.php")
    suspend fun buscarPaciente(
        @QueryMap parametros: Map<String, String> = emptyMap()
    ): Response<JsonElement>

    @GET("citas_registradas.php")
    suspend fun citasRegistradas(
        @QueryMap parametros: Map<String, String> = emptyMap()
    ): Response<JsonElement>

    @GET("detalle_citas.php")
    suspend fun detalleCitas(
        @QueryMap parametros: Map<String, String> = emptyMap()
    ): Response<JsonElement>

    @GET("obtener_citas.php")
    suspend fun obtenerCitas(
        @QueryMap parametros: Map<String, String> = emptyMap()
    ): Response<JsonElement>

    @GET("obtener_especialidades.php")
    suspend fun obtenerEspecialidades(): Response<JsonElement>

    @GET("obtener_horarios.php")
    suspend fun obtenerHorarios(
        @QueryMap parametros: Map<String, String> = emptyMap()
    ): Response<JsonElement>

    @GET("obtener_medico.php")
    suspend fun obtenerMedico(
        @QueryMap parametros: Map<String, String> = emptyMap()
    ): Response<JsonElement>

    @GET("obtener_paciente.php")
    suspend fun obtenerPaciente(
        @QueryMap parametros: Map<String, String> = emptyMap()
    ): Response<JsonElement>

    @GET("obtener_usuarios.php")
    suspend fun obtenerUsuarios(): Response<JsonElement>

    @FormUrlEncoded
    @POST("actualizar_especialidad.php")
    suspend fun actualizarEspecialidad(
        @FieldMap datos: Map<String, String>
    ): Response<JsonElement>

    @FormUrlEncoded
    @POST("actualizar_horario.php")
    suspend fun actualizarHorario(
        @FieldMap datos: Map<String, String>
    ): Response<JsonElement>

    @FormUrlEncoded
    @POST("actualizar_medico.php")
    suspend fun actualizarMedico(
        @FieldMap datos: Map<String, String>
    ): Response<JsonElement>

    @FormUrlEncoded
    @POST("actualizar_paciente.php")
    suspend fun actualizarPaciente(
        @FieldMap datos: Map<String, String>
    ): Response<JsonElement>

    @FormUrlEncoded
    @POST("asignar_rol.php")
    suspend fun asignarRol(
        @FieldMap datos: Map<String, String>
    ): Response<JsonElement>

    @FormUrlEncoded
    @POST("cancelar_cita.php")
    suspend fun cancelarCita(
        @FieldMap datos: Map<String, String>
    ): Response<JsonElement>

    @FormUrlEncoded
    @POST("modificar_cita.php")
    suspend fun modificarCita(
        @FieldMap datos: Map<String, String>
    ): Response<JsonElement>

    @FormUrlEncoded
    @POST("registrar_cita.php")
    suspend fun registrarCita(
        @FieldMap datos: Map<String, String>
    ): Response<JsonElement>

    @FormUrlEncoded
    @POST("registrar_especialidad.php")
    suspend fun registrarEspecialidad(
        @FieldMap datos: Map<String, String>
    ): Response<JsonElement>

    @FormUrlEncoded
    @POST("registrar_horario.php")
    suspend fun registrarHorario(
        @FieldMap datos: Map<String, String>
    ): Response<JsonElement>

    @FormUrlEncoded
    @POST("registrar_medico.php")
    suspend fun registrarMedico(
        @FieldMap datos: Map<String, String>
    ): Response<JsonElement>

    @FormUrlEncoded
    @POST("registrar_paciente.php")
    suspend fun registrarPaciente(
        @FieldMap datos: Map<String, String>
    ): Response<JsonElement>

    @FormUrlEncoded
    @POST("registrar_usuario.php")
    suspend fun registrarUsuario(
        @FieldMap datos: Map<String, String>
    ): Response<JsonElement>

}
