package com.example.citasmed.Modelo

class clsModelo {

    fun nombreUsuario(usuario: String): String {
        return usuario
    }

    fun datoContra(contra: String): String {
        return contra
    }

    fun validarInicioSesion(usuario: String, contra: String): Boolean {
        return nombreUsuario(usuario) == "admin" &&
                datoContra(contra) == "12345"
    }
}