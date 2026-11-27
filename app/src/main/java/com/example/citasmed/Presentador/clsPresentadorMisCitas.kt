
package com.example.citasmed.Presentador

import com.example.citasmed.vista.misCitasPaciente
import com.example.citasmed.Modelo.clsModeloPaciente
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.widget.Toast

class clsPresentadorMisCitas(private val vista: misCitasPaciente) {

    private val modelo = clsModeloPaciente()

    fun cargarCitas() {
        vista.lifecycleScope.launch {
            val citas = modelo.consultarMisCitas()
            vista.mostrarCitas(citas)
        }
    }

    fun cancelarCita(idCita: Int) {
        vista.lifecycleScope.launch {

            val respuesta = modelo.cancelarCita(idCita)

            if (respuesta != null) {

                Toast.makeText(vista, "Cita cancelada correctamente", Toast.LENGTH_SHORT).show()

                val citas = modelo.consultarMisCitas()
                vista.mostrarCitas(citas)

            } else {

                Toast.makeText(vista, "No se pudo cancelar la cita", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun reprogramarCita(idCita: Int, fecha: String, hora: String) {
        vista.lifecycleScope.launch {

            val respuesta = modelo.reprogramarCita(idCita, fecha, hora)

            if (respuesta != null) {

                Toast.makeText(vista, "Cita reprogramada correctamente", Toast.LENGTH_SHORT).show()

                val citas = modelo.consultarMisCitas()
                vista.mostrarCitas(citas)

            } else {

                Toast.makeText(vista, "No se pudo reprogramar la cita", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
