
package com.example.citasmed.Presentador

import com.example.citasmed.vista.nuevaCitaPaciente
import com.example.citasmed.Modelo.clsModelo
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import com.example.citasmed.Modelo.clsModeloPaciente

class clsPresentadorNuevaCita(private val vista: nuevaCitaPaciente) {

    private val modelo = clsModelo()
    private val modeloPaciente = clsModeloPaciente()

    fun cargarEspecialidades() {
        vista.lifecycleScope.launch {
            try {
                val respuesta = modelo.consultarEspecialidades()

                if (respuesta.isSuccessful) {
                    val datos = respuesta.body()

                    if (datos != null) {
                        val ids = mutableListOf<String>()
                        val nombres = mutableListOf<String>()

                        for (especialidad in datos.jsonArray) {
                            ids.add(especialidad.jsonObject["id_especialidad"]!!.jsonPrimitive.content)

                            nombres.add(especialidad.jsonObject["nombre"]!!.jsonPrimitive.content)
                        }

                        val adaptador = ArrayAdapter(vista, android.R.layout.simple_spinner_item, nombres)

                        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                        vista.mostrarEspecialidades(ids, adaptador)
                    }
                } else {
                    Toast.makeText(vista, "No se pudieron cargar las especialidades", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Toast.makeText(vista, e.message ?: "Error al cargar especialidades", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun cargarMedicos(idEspecialidad: String) {
        vista.lifecycleScope.launch {
            try {
                val respuesta = modelo.consultarMedicos(idEspecialidad)

                if (respuesta.isSuccessful) {
                    val datos = respuesta.body()

                    if (datos != null) {
                        val ids = mutableListOf<String>()
                        val nombres = mutableListOf<String>()

                        for (medico in datos.jsonArray) {
                            ids.add(medico.jsonObject["id_medico"]!!.jsonPrimitive.content)

                            val nombre = medico.jsonObject["nombre"]!!.jsonPrimitive.content

                            val apellidos = medico.jsonObject["apellidos"]!!.jsonPrimitive.content

                            nombres.add("$nombre $apellidos")
                        }

                        val adaptador = ArrayAdapter(vista, android.R.layout.simple_spinner_item, nombres)

                        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                        vista.mostrarMedicos(ids, adaptador)
                    }
                } else {
                    Toast.makeText(vista, "No se pudieron cargar los médicos", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Toast.makeText(vista, e.message ?: "Error al cargar médicos", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun cargarHorarios(idMedico: String, fecha: String) {
        vista.lifecycleScope.launch {
            try {
                val respuesta = modelo.consultarHorarios(idMedico, fecha)

                if (respuesta.isSuccessful) {
                    val datos = respuesta.body()

                    if (datos != null) {
                        val horarios = mutableListOf<String>()

                        for (horario in datos.jsonArray) {
                            horarios.add(horario.jsonObject["hora"]!!.jsonPrimitive.content)
                        }

                        val adaptador = ArrayAdapter(
                            vista,
                            android.R.layout.simple_spinner_item,
                            horarios
                        )

                        adaptador.setDropDownViewResource(
                            android.R.layout.simple_spinner_dropdown_item
                        )

                        vista.mostrarHorarios(adaptador)

                        if (horarios.isEmpty()) {
                            Toast.makeText(vista, "No hay horarios disponibles para esta fecha", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(vista, "No se pudieron consultar los horarios", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Toast.makeText(vista, e.message ?: "Error al consultar horarios", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun confirmarCita(idMedico: String, fecha: String, hora: String, motivo: String) {
        vista.lifecycleScope.launch {
            try {
                val respuesta= modelo.registrarCita(idMedico, fecha, hora, motivo)
                if (respuesta.isSuccessful){
                    val datos = respuesta.body()
                    if (datos!= null){
                        val resultado = datos.jsonObject
                        val exito = resultado["exito"]!!.jsonPrimitive.content.toBoolean()
                        val mensaje = resultado["mensaje"]!!.jsonPrimitive.content
                        Toast.makeText(vista, mensaje, Toast.LENGTH_LONG).show()
                        if (exito){
                            vista.finish()
                        }

                    }
                }else{
                    Toast.makeText(vista,"No se pudo registrar la cita. Codigo: ${respuesta.code()}", Toast.LENGTH_SHORT).show()

                }
            } catch (e: Exception){
                Toast.makeText(vista, e.message?: "Error al registrar su sita", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun reprogramarCita(idCita: Int, fecha: String, hora: String) {
        vista.lifecycleScope.launch {
            try {
                val respuesta = modeloPaciente.reprogramarCita(idCita, fecha, hora)

                if (respuesta != null) {
                    val resultado = respuesta.jsonObject
                    val exito = resultado["exito"]?.jsonPrimitive?.content?.toBoolean() ?: false
                    val mensaje = resultado["mensaje"]?.jsonPrimitive?.content ?: "No se pudo reprogramar la cita"
                    Toast.makeText(vista, mensaje, Toast.LENGTH_LONG).show()

                    if (exito) {
                        vista.finish()
                    }
                } else {
                    Toast.makeText(vista, "No se pudo reprogramar la cita", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Toast.makeText(vista, e.message ?: "Error al reprogramar la cita", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
