package com.example.citasmed.Presentador

import com.example.citasmed.Modelo.clsModelo
import com.example.citasmed.vista.MainActivity

class clsPresentador(private val vista: MainActivity) {

    private val modelo = clsModelo()

    fun inicioSesion(usuario: String, contra: String, perfil: String) {
        if (modelo.validarInicioSesion(usuario, contra)) {
            vista.abrirVentana(perfil)
        } else {
            vista.mostrarMensaje("Usuario o contraseña incorrectos")
        }
    }
}