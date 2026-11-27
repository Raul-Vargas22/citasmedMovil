package com.example.citasmed.Presentador

import com.example.citasmed.Modelo.clsLoginModelo
import com.example.citasmed.Vista.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class clsPresentadorLogin(private val view: LoginActivity) {

    private val modelo = clsLoginModelo()

    fun realizarLogin(correo: String, contrasena: String) {

        if (correo.isEmpty() || contrasena.isEmpty()) {
            view.mostrarError("Por favor, ingresa correo y contraseña")
            return
        }

        CoroutineScope(Dispatchers.Main).launch {

            try {

                val respuesta = withContext(Dispatchers.IO) {
                    modelo.autenticarUsuario(correo, contrasena)
                }

                if (respuesta.exito) {
                    view.loginExitoso(
                        respuesta.mensaje,
                        respuesta.nombre
                    )
                } else {
                    view.mostrarError(respuesta.mensaje)
                }

            } catch (e: Exception) {

                view.mostrarError(
                    "Error de conexión: ${e.localizedMessage ?: "Error desconocido"}"
                )
            }
        }
    }
}