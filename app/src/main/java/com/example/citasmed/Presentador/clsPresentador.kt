package com.example.citasmed.Presentador

import androidx.lifecycle.lifecycleScope
import com.example.citasmed.Modelo.clsModelo
import com.example.citasmed.vista.MainActivity
import kotlinx.coroutines.launch

class clsPresentador(private val vista: MainActivity) {

    private val modelo = clsModelo()

    fun inicioSesion(usuario: String, contra: String, perfil: String) {

        vista.lifecycleScope.launch {
            try {
                clsModelo.sesion = null

                val resultado = modelo.validarInicioSesion(usuario, contra)

                val rol = when (perfil) {
                    "Administrador" -> "administrador"
                    "Doctor" -> "medico"
                    "Empleado" -> "personal_asistencia_medica"
                    "Paciente" -> "paciente"
                    else -> ""
                }

                if (resultado == null) {
                    vista.mostrarMensaje("Usuario o contraseña incorrectos")
                } else if (resultado.rol != rol) {
                    vista.mostrarMensaje("Selecciona el perfil correcto")
                } else {
                    clsModelo.sesion = resultado
                    vista.abrirVentana(perfil)
                }

            } catch (e: Exception) {
                vista.mostrarMensaje(e.message ?: "Error desconocido")
            }
        }
    }
}