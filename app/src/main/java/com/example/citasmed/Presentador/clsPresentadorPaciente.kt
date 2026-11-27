package com.example.citasmed.Presentador

import com.example.citasmed.vista.menuPaciente

class clsPresentadorPaciente(private val vista: menuPaciente) {
    fun nuevaCita() {

    }

    fun consultarCita() {

    }

    fun mostrarMisCitas() {
        vista.abrirMisCitas()
    }

    fun mostrarMedicos() {
        vista.abrirMedicos()
    }
}