package com.example.citasmed.Modelo

import com.example.citasmed.Red.Retrofit

class clsLoginModelo {

    suspend fun autenticarUsuario(
        correo: String,
        contrasena: String
    ): List<dataUsuarios> {
        return Retrofit.apiService.iniciarSesion(correo, contrasena)
    }
}