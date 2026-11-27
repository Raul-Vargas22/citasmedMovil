package com.example.citasmed.Modelo

import com.example.citasmed.Red.Retrofit

class modeloDoctor {

    suspend fun obtenerDoctor(idDoctor: Int): List<dataDoctor> {
        return Retrofit.apiService.obtenerMedico(idDoctor)
    }

    suspend fun obtenerPaciente(idPaciente: Int): List<dataPaciente> {
        return Retrofit.apiService.obtenerPaciente(idPaciente)
    }

    suspend fun obtenerCitas(idDoctor: Int): List<dataCita> {
        return Retrofit.apiService.obtenerCitas(idDoctor)
    }

    suspend fun obtenerHorarios(idDoctor: Int): List<dataHorario> {
        return Retrofit.apiService.obtenerHorarios(idDoctor)
    }
}