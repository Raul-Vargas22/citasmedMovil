
package com.example.citasmed.vista

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.citasmed.R
import com.example.citasmed.Modelo.dataCitaPaciente
import com.example.citasmed.Presentador.clsPresentadorMisCitas
import java.time.LocalDate
import android.view.View

class misCitasPaciente : AppCompatActivity() {

    private lateinit var presentador: clsPresentadorMisCitas
    private lateinit var navegacion: clsNavegacionPaciente

    private lateinit var contenedorProximasCitas: LinearLayout
    private lateinit var contenedorHistorialCitas: LinearLayout
    private lateinit var txtRegresarMisCitas: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_citas_paciente)
        txtRegresarMisCitas = findViewById(R.id.txtRegresarMisCitas)
        txtRegresarMisCitas.setOnClickListener {
            finish()
        }
        navegacion = clsNavegacionPaciente(this)
        navegacion.configurarNavegacion()

        presentador = clsPresentadorMisCitas(this)

        contenedorProximasCitas = findViewById(R.id.contenedorProximasCitas)

        contenedorHistorialCitas = findViewById(R.id.contenedorHistorialCitas)

        presentador.cargarCitas()
    }

    fun mostrarCitas(citas: List<dataCitaPaciente>) {

        contenedorProximasCitas.removeAllViews()
        contenedorHistorialCitas.removeAllViews()

        if (citas.isEmpty()) {
            mostrarMensaje(contenedorProximasCitas, "No tienes citas registradas.")
            return
        }

        val hoy = LocalDate.now()

        for (cita in citas) {

            val fechaCita = try {
                LocalDate.parse(cita.fecha)
            } catch (e: Exception) {
                null
            }

            if (fechaCita != null && fechaCita.isBefore(hoy)) {

                crearTarjeta(cita, contenedorHistorialCitas)

            } else {

                crearTarjeta(cita, contenedorProximasCitas)
            }
        }

        if (contenedorProximasCitas.childCount == 0) {
            mostrarMensaje(contenedorProximasCitas, "No tienes próximas citas.")
        }

        if (contenedorHistorialCitas.childCount == 0) {
            mostrarMensaje(contenedorHistorialCitas, "Todavía no tienes citas anteriores.")
        }
    }

    private fun crearTarjeta(cita: dataCitaPaciente, contenedor: LinearLayout) {

        val tarjeta = LayoutInflater.from(this).inflate(R.layout.item_cita_paciente, contenedor, false)

        val tvNombreMedico = tarjeta.findViewById<TextView>(R.id.tvNombreMedico)

        val tvEspecialidadPaciente = tarjeta.findViewById<TextView>(R.id.tvEspecialidadPaciente)

        val tvFechaHora = tarjeta.findViewById<TextView>(R.id.tvFechaHora)

        val tvEstadoCita = tarjeta.findViewById<TextView>(R.id.tvEstadoCita)

        val btnReprogramar = tarjeta.findViewById<Button>(R.id.btnReprogramarCita)

        val btnCancelar =tarjeta.findViewById<Button>(R.id.btnCancelarCita)

        tvNombreMedico.text = "Dr. ${cita.nombre_medico}"

        tvEspecialidadPaciente.text = "${cita.especialidad} - ${cita.nombre_paciente}"

        tvFechaHora.text = "${cita.fecha} - ${cita.hora}"

        tvEstadoCita.text = cita.estado

        val permiteAcciones =
            (cita.estado.equals("pendiente", ignoreCase = true) ||
                    cita.estado.equals("confirmada", ignoreCase = true)) &&
                    contenedor == contenedorProximasCitas

        if (!permiteAcciones) {
            btnReprogramar.visibility = View.GONE
            btnCancelar.visibility = View.GONE
        }


        btnReprogramar.setOnClickListener {
            val intent = android.content.Intent(this, nuevaCitaPaciente::class.java)
            intent.putExtra("modo", "reprogramar")
            intent.putExtra("id_cita", cita.id_cita)
            intent.putExtra("id_medico", cita.id_medico)
            startActivity(intent)
        }

        btnCancelar.setOnClickListener {
            presentador.cancelarCita(cita.id_cita)
        }

        contenedor.addView(tarjeta)
    }

    private fun mostrarMensaje(contenedor: LinearLayout, mensaje: String) {
        val texto = TextView(this)
        texto.text = mensaje
        texto.textSize = 13f
        texto.setPadding(8, 12, 8, 12)
        contenedor.addView(texto)
    }
}
