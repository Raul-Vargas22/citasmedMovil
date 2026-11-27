package com.example.citasmed.Interface

import com.example.citasmed.Modelo.dataCita
import com.example.citasmed.Modelo.dataDoctor
import com.example.citasmed.Modelo.dataEspecialidad
import com.example.citasmed.Modelo.dataHorario
import com.example.citasmed.Modelo.dataPaciente
import com.example.citasmed.Modelo.dataRespuesta
import com.example.citasmed.Modelo.dataUsuarios
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface apiService {

    @GET("obtener_citas.php")
    suspend fun obtenerCitas(): List<dataCita>

    @GET("detalle_citas.php")
    suspend fun detalleCita(
        @Query(value = "cita_id") citaId: Int
    ): List<dataCita>

    @GET("obtener_especialidades.php")
    suspend fun obtenerEspecialidades(): List<dataEspecialidad>

    @GET("obtener_horarios.php")
    suspend fun obtenerHorarios(
        @Query(value = "id_medico") idMedico: Int
    ): List<dataHorario>

    @GET("obtener_medico.php")
    suspend fun obtenerMedico(
        @Query(value = "medico_id") medicoId: Int
    ): List<dataDoctor>

    @GET("obtener_paciente.php")
    suspend fun obtenerPaciente(
        @Query(value = "paciente_id") pacienteId: Int
    ): List<dataPaciente>

    @GET("obtener_usuarios.php")
    suspend fun obtenerUsuarios(): List<dataUsuarios>

    @FormUrlEncoded
    @POST("login.php")
    suspend fun iniciarSesion(
        @Field("correo") correo: String,
        @Field("contrasena") contrasena: String
    ): List<dataUsuarios>

    @FormUrlEncoded
    @POST("registrar_cita.php")
    suspend fun registrarCita(
        @Field("id_paciente") idPaciente: Int,
        @Field("id_medico") idMedico: Int,
        @Field("fecha") fecha: String,
        @Field("hora") hora: String,
        @Field("motivo") motivo: String
    ): dataRespuesta

    @FormUrlEncoded
    @POST("registrar_especialidad.php")
    suspend fun registrarEspecialidad(
        @Field("nombre") nombre: String,
        @Field("descripcion") descripcion: String
    ): dataRespuesta

    @FormUrlEncoded
    @POST("registrar_horario.php")
    suspend fun registrarHorario(
        @Field("id_medico") idMedico: Int,
        @Field("fecha") fecha: String,
        @Field("hora") hora: String
    ): dataRespuesta

    @FormUrlEncoded
    @POST("registrar_medico.php")
    suspend fun registrarMedico(
        @Field("nombre") nombre: String,
        @Field("apellidos") apellidos: String,
        @Field("correo") correo: String,
        @Field("contrasena") contrasena: String,
        @Field("id_especialidad") idEspecialidad: Int,
        @Field("cedula_profesional") cedulaProfesional: String,
        @Field("telefono") telefono: String,
        @Field("consultorio") consultorio: String
    ): dataRespuesta

    @FormUrlEncoded
    @POST("registrar_paciente.php")
    suspend fun registrarPaciente(
        @Field("nombre") nombre: String,
        @Field("apellidos") apellidos: String,
        @Field("correo") correo: String,
        @Field("contrasena") contrasena: String,
        @Field("curp") curp: String,
        @Field("telefono") telefono: String,
        @Field("fecha_nacimiento") fechaNacimiento: String
    ): dataRespuesta

    @FormUrlEncoded
    @POST("registrar_usuario.php")
    suspend fun registrarUsuario(
        @Field("rol") rol: String,
        @Field("nombre") nombre: String,
        @Field("apellidos") apellidos: String,
        @Field("correo") correo: String,
        @Field("contrasena") contrasena: String,
        @Field("telefono") telefono: String
    ): dataRespuesta
}