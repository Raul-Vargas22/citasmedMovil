package com.example.citasmed.Presentador

import com.example.citasmed.Modelo.clsModeloAgendaEmpleado
import com.example.citasmed.Modelo.clsModeloEmpleado
import com.example.citasmed.Modelo.dataCitaEmpleado
import com.example.citasmed.Modelo.dataHorarioEmpleado
import com.example.citasmed.Modelo.dataMedicoEmpleado
import com.example.citasmed.Modelo.dataPacienteEmpleado

class clsPresentadorAgendaEmpleado {
    private val agenda = clsModeloAgendaEmpleado()
    private val pacientes = clsModeloEmpleado()

    suspend fun citas(filtro: String = ""): List<dataCitaEmpleado> {
        return agenda.consultarCitas(filtro)
    }

    suspend fun medicos(): List<dataMedicoEmpleado> {
        return agenda.consultarMedicos()
    }

    suspend fun horarios(idMedico: Int): List<dataHorarioEmpleado> {
        return agenda.consultarHorarios(idMedico)
    }

    suspend fun pacientes(): List<dataPacienteEmpleado> {
        return pacientes.buscarPacientes("")
    }

    suspend fun registrar(idPaciente: Int, idMedico: Int, horario: dataHorarioEmpleado, motivo: String) {
        require(idPaciente > 0 && idMedico > 0) { "Selecciona un paciente y un médico" }
        require(motivo.trim().length >= 3) { "Escribe el motivo de la cita" }
        agenda.registrar(idPaciente, idMedico, horario, motivo.trim())
    }

    suspend fun reprogramar(cita: dataCitaEmpleado, horario: dataHorarioEmpleado) {
        require(cita.estado == "pendiente" || cita.estado == "confirmada") { "La cita no puede reprogramarse" }
        agenda.reprogramar(cita.id, horario)
    }

    suspend fun cancelar(cita: dataCitaEmpleado) {
        require(cita.estado == "pendiente" || cita.estado == "confirmada") { "La cita no puede cancelarse" }
        agenda.cancelar(cita.id)
    }
}
