package com.example.citasmed.vista

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.citasmed.R
import com.example.citasmed.Presentador.clsPresentadorNuevaCita
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.app.DatePickerDialog
import android.widget.Toast
import java.util.Calendar
import android.widget.TextView

class nuevaCitaPaciente : AppCompatActivity() {

    private lateinit var spnEspecialidadPaciente: Spinner
    private lateinit var spnMedicoPaciente: Spinner
    private lateinit var spnHorarioPaciente: Spinner

    private lateinit var txtFechaPaciente: EditText
    private lateinit var txtTelefonoPaciente: EditText
    private lateinit var txtMotivoPaciente: EditText

    private lateinit var btnCancelarNuevaCita: Button
    private lateinit var btnConfirmarNuevaCita: Button
    private lateinit var txtRegresarNuevaCita: TextView

    private lateinit var presentador: clsPresentadorNuevaCita
    private lateinit var navegacion: clsNavegacionPaciente
    private var idsEspecialidades = mutableListOf<String>()
    private var idsMedicos = mutableListOf<String>()
    private var fechaApi = ""
    private var modo = "nueva"
    private var idCita = 0
    private var idMedicoReprogramar = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nueva_cita_paciente)
        presentador = clsPresentadorNuevaCita(this)
        modo = intent.getStringExtra("modo") ?: "nueva"
        idCita = intent.getIntExtra("id_cita", 0)
        idMedicoReprogramar = intent.getIntExtra("id_medico", 0)
        spnEspecialidadPaciente = findViewById(R.id.spnEspecialidadPaciente)
        spnMedicoPaciente = findViewById(R.id.spnMedicoPaciente)
        spnHorarioPaciente = findViewById(R.id.spnHorarioPaciente)

        txtFechaPaciente = findViewById(R.id.txtFechaPaciente)
        txtTelefonoPaciente = findViewById(R.id.txtTelefonoPaciente)
        txtMotivoPaciente = findViewById(R.id.txtMotivoPaciente)

        btnCancelarNuevaCita = findViewById(R.id.btnCancelarNuevaCita)
        btnConfirmarNuevaCita = findViewById(R.id.btnConfirmarNuevaCita)

        btnCancelarNuevaCita.setOnClickListener(this::ejecutarCancelar)
        btnConfirmarNuevaCita.setOnClickListener(this::ejecutarConfirmar)
        txtRegresarNuevaCita = findViewById(R.id.txtRegresarNuevaCita)

        txtRegresarNuevaCita.setOnClickListener {
            finish()
        }

        navegacion = clsNavegacionPaciente(this)
        navegacion.configurarNavegacion()

        if (modo == "reprogramar") {
            findViewById<View>(R.id.lblEspecialidadPaciente).visibility = View.GONE
            spnEspecialidadPaciente.visibility = View.GONE
            findViewById<View>(R.id.lblMedicoPaciente).visibility = View.GONE
            spnMedicoPaciente.visibility = View.GONE
            findViewById<View>(R.id.lblTelefonoPaciente).visibility = View.GONE
            txtTelefonoPaciente.visibility = View.GONE
            findViewById<View>(R.id.lblMotivoPaciente).visibility = View.GONE
            txtMotivoPaciente.visibility = View.GONE
        }

        if (modo == "reprogramar") {

            findViewById<TextView>(R.id.tvTituloCita).text = "Reprogramar cita"

            findViewById<TextView>(R.id.tvDescripcionCita).text = "Modifica la fecha y el horario de tu cita médica."

            findViewById<TextView>(R.id.tvEtiquetaCita).text = "REPROGRAMACIÓN"

            findViewById<TextView>(R.id.tvSubtituloCita).text = "Elige una fecha y consulta los horarios disponibles."
            findViewById<TextView>(R.id.tvDetalleTarjetaCita).text = "Consulta los horarios disponibles del médico asignado a tu cita."
            btnConfirmarNuevaCita.text = "Guardar cambios"
        }


        if (modo == "nueva") {
            presentador.cargarEspecialidades()
        }
        spnEspecialidadPaciente.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (idsEspecialidades.isNotEmpty()) {
                        presentador.cargarMedicos(idsEspecialidades[position])
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        spnMedicoPaciente.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (idsMedicos.isNotEmpty() && fechaApi.isNotEmpty()) {
                        presentador.cargarHorarios(idsMedicos[position], fechaApi)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        txtFechaPaciente.setOnClickListener {
            seleccionarFecha()
        }
    }
    private fun ejecutarCancelar(v: View) {
        finish()
    }


    private fun ejecutarConfirmar(v: View) {

        if (fechaApi.isEmpty()) {
            Toast.makeText(this, "Selecciona una fecha por favor", Toast.LENGTH_SHORT).show()
            return
        }

        if (spnHorarioPaciente.selectedItem == null) {
            Toast.makeText(this, "Selecciona un horario por favor", Toast.LENGTH_SHORT).show()
            return
        }

        val hora = spnHorarioPaciente.selectedItem.toString()

        if (modo == "reprogramar") {
            presentador.reprogramarCita(idCita, fechaApi, hora)
            return
        }

        if (idsMedicos.isEmpty()) {
            Toast.makeText(this, "Selecciona un médico por favor", Toast.LENGTH_SHORT).show()
            return
        }

        val posicion = spnMedicoPaciente.selectedItemPosition
        val idMedico = idsMedicos[posicion]
        val motivo = txtMotivoPaciente.text.toString().trim()

        if (motivo.isEmpty()) {
            Toast.makeText(this, "Escribe el motivo de la consulta", Toast.LENGTH_SHORT).show()
            return
        }

        presentador.confirmarCita(idMedico, fechaApi, hora, motivo)
    }


    fun mostrarEspecialidades(ids: MutableList<String>, adaptador: ArrayAdapter<String>) {
        idsEspecialidades = ids
        spnEspecialidadPaciente.adapter = adaptador
    }
    fun mostrarMedicos(ids: MutableList<String>, adaptador: ArrayAdapter<String>) {
        idsMedicos = ids
        spnMedicoPaciente.adapter = adaptador
    }
    fun mostrarHorarios(adaptador: ArrayAdapter<String>) {
        spnHorarioPaciente.adapter = adaptador
    }
    private fun seleccionarFecha() {

        val calendario = Calendar.getInstance()

        val selector = DatePickerDialog(
            this,
            { _, año, mes, dia ->

                fechaApi = String.format("%04d-%02d-%02d", año, mes + 1, dia)

                txtFechaPaciente.setText(String.format("%02d/%02d/%04d", dia, mes + 1, año))

                if (modo == "reprogramar") {
                    presentador.cargarHorarios(idMedicoReprogramar.toString(), fechaApi)
                } else {
                    if (idsMedicos.isNotEmpty()) {
                        val posicion = spnMedicoPaciente.selectedItemPosition
                        if (posicion >= 0) {
                            presentador.cargarHorarios(idsMedicos[posicion], fechaApi)
                        }
                    }
                }

            },
            calendario.get(Calendar.YEAR),
            calendario.get(Calendar.MONTH),
            calendario.get(Calendar.DAY_OF_MONTH)
        )

        selector.datePicker.minDate = calendario.timeInMillis
        selector.show()
    }
}