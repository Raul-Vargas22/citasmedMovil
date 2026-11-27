package com.example.citasmed.Presentador

import com.example.citasmed.Modelo.clsModeloAdmin
import com.example.citasmed.Modelo.dataEspecialidadHorariosAdmin
import com.example.citasmed.Modelo.dataMedicoAdmin

class clsPresentadorAdmin {
    private val modeloAdmin = clsModeloAdmin()

    private val medicosAdmin = mutableListOf(
        dataMedicoAdmin(1, "Dra. Elena Ramírez", "Medicina general", "Lun a Vie · 9:00-14:00"),
        dataMedicoAdmin(2, "Dr. Luis Méndez", "Cardiología", "Lun a Vie · 9:00-14:00"),
        dataMedicoAdmin(3, "Dr. Carlos Sánchez", "Pediatría", "Lun a Vie · 9:00-14:00")
    )

    fun guardarMedico(
        id: Int?,
        nombre: String,
        especialidad: String,
        horario: String
    ): String? {
        val nombreLimpio = nombre.trim()
        val especialidadLimpia = especialidad.trim()
        val horarioLimpio = horario.trim()

        if (nombreLimpio.length < 3) {
            return "Escriba un nombre válido"
        }
        if (especialidadLimpia.length < 3) {
            return "Escriba una especialidad válida"
        }
        if (horarioLimpio.length < 3) {
            return "Escriba un horario válido"
        }

        if (id == null) {
            val nuevoId = (medicosAdmin.maxOfOrNull { it.id } ?: 0) + 1
            medicosAdmin.add(
                dataMedicoAdmin(
                    id = nuevoId,
                    nombre = nombreLimpio,
                    especialidad = especialidadLimpia,
                    horario = horarioLimpio
                )
            )
        } else {
            val indice = medicosAdmin.indexOfFirst { it.id == id }
            if (indice != -1) {
                medicosAdmin[indice] = dataMedicoAdmin(
                    id = id,
                    nombre = nombreLimpio,
                    especialidad = especialidadLimpia,
                    horario = horarioLimpio
                )
            }
        }

        return null
    }

    fun obtenerMedicos(): List<dataMedicoAdmin> {
        return medicosAdmin.toList()
    }
    private val especialidadesHorarios = mutableListOf(
        dataEspecialidadHorariosAdmin(1, "Cardiología", "Lun - Vie · 08:00 - 14:00"),
        dataEspecialidadHorariosAdmin(2, "Pediatría", "Mar - Jue · 10:00 - 16:00")
    )

    fun guardarEspecHorarios(id: Int?, nombre: String, descripcion: String): String {
        val nombreLimpio = nombre.trim()
        val descripcionLimpia = descripcion.trim()

        if (nombreLimpio.length < 3) {
            return "Escriba un nombre valido"
        }
        if (descripcionLimpia.length < 3) {
            return "Escriba una descripcion valida"
        }
        if (id == null) {
            agregarEspecialidad(nombreLimpio, descripcionLimpia)
        } else {
            actualizarEspecialidad(id, nombreLimpio, descripcionLimpia)
        }
        return "Guardado correctamente"
    }

    fun obtenerEspecialidades(): List<dataEspecialidadHorariosAdmin> {
        return especialidadesHorarios.toList()
    }

    fun agregarEspecialidad(nombre: String, descripcion: String) {
        val nuevoId = (especialidadesHorarios.maxOfOrNull { it.id } ?: 0) + 1
        especialidadesHorarios.add(dataEspecialidadHorariosAdmin(nuevoId, nombre, descripcion))
    }

    fun actualizarEspecialidad(id: Int, nombre: String, descripcion: String) {
        val index = especialidadesHorarios.indexOfFirst { it.id == id }
        if (index != -1) {
            especialidadesHorarios[index] = dataEspecialidadHorariosAdmin(id, nombre, descripcion)
        }
    }
}