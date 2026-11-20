package com.example.citasmed.Modelo

class clsModeloEmpleado {

    private val pacientes = mutableListOf(
        dataPacienteEmpleado(1, "Ana López", "7713501991"),
        dataPacienteEmpleado(2, "Roberto Díaz", "7712487630"),
        dataPacienteEmpleado(3, "María Hernández", "7714192856")
    )

    fun totalPacientes(): Int {
        return pacientes.size
    }

    fun buscarPacientes(texto: String): List<dataPacienteEmpleado> {
        val busqueda = texto.trim()
        return pacientes.filter {
            it.nombre.contains(busqueda, ignoreCase = true) || it.telefono.contains(busqueda)
        }
    }

    fun registrarPaciente(nombre: String, telefono: String) {
        val nuevoId = (pacientes.maxOfOrNull { it.id } ?: 0) + 1
        pacientes.add(dataPacienteEmpleado(nuevoId, nombre, telefono))
    }

    fun actualizarPaciente(id: Int, nombre: String, telefono: String) {
        val paciente = pacientes.find { it.id == id }
        if (paciente != null) {
            paciente.nombre = nombre
            paciente.telefono = telefono
        }
    }
}
