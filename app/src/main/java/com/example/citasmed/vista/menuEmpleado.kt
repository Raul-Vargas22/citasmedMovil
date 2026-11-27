package com.example.citasmed.vista

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.citasmed.Modelo.clsModeloEmpleado
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.citasmed.R
import com.example.citasmed.Modelo.dataPacienteEmpleado
import com.example.citasmed.Modelo.dataMedicoEmpleado
import com.example.citasmed.Modelo.dataHorarioEmpleado
import com.example.citasmed.Modelo.dataCitaEmpleado
import com.example.citasmed.Presentador.clsPresentadorAgendaEmpleado
import com.example.citasmed.Presentador.clsPresentadorEmpleado

class menuEmpleado : AppCompatActivity() {
    private val presentador = clsPresentadorEmpleado()
    private val agenda = clsPresentadorAgendaEmpleado()
    private var listaMedicos: List<dataMedicoEmpleado> = emptyList()
    private var pantalla = 0
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var solicitudPacientes: Job? = null
    private var solicitudCitas: Job? = null
    private var solicitudMedicos: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mostrarInicio()
    }

    private fun abrir(layout: Int, seccion: Int) {
        if (seccion != 1) solicitudPacientes?.cancel()
        if (seccion != 2) solicitudCitas?.cancel()
        if (seccion != 3) solicitudMedicos?.cancel()
        pantalla = seccion
        setContentView(layout)
        findViewById<TextView>(R.id.txtCerrarSesionEmpleado).setOnClickListener {
            clsModelo.sesion = null
            finish()
        }
        findViewById<LinearLayout>(R.id.navInicioEmpleado).setOnClickListener { mostrarInicio() }
        findViewById<LinearLayout>(R.id.navPacientesEmpleado).setOnClickListener { mostrarPacientes() }
        findViewById<LinearLayout>(R.id.navCitasEmpleado).setOnClickListener { mostrarCitas() }
        findViewById<LinearLayout>(R.id.navMedicosEmpleado).setOnClickListener { mostrarMedicos() }
        val nombres = intArrayOf(R.id.labelNavInicioEmpleado, R.id.labelNavPacientesEmpleado,
            R.id.labelNavCitasEmpleado, R.id.labelNavMedicosEmpleado)
        val iconos = intArrayOf(R.id.iconNavInicioEmpleado, R.id.iconNavPacientesEmpleado,
            R.id.iconNavCitasEmpleado, R.id.iconNavMedicosEmpleado)
        for (i in 0..3) {
            val color = if (i == seccion) Color.parseColor("#2F80B7") else Color.parseColor("#8796A4")
            findViewById<TextView>(nombres[i]).setTextColor(color)
            findViewById<TextView>(iconos[i]).setTextColor(color)
        }
     //   val nombre = clsModeloEmpleado.sesion?.nombre.orEmpty().trim()
        val iniciales = nombre.split(" ").filter { it.isNotBlank() }.take(2)
            .joinToString("") { it.take(1).uppercase() }
        if (iniciales.isNotBlank()) findViewById<TextView>(R.id.txtInicialesEmpleado).text = iniciales
    }

    private fun mostrarInicio() {
        abrir(R.layout.activity_menu_empleado, 0)
        findViewById<LinearLayout>(R.id.btnPacientesEmpleado).setOnClickListener { mostrarPacientes() }
        findViewById<LinearLayout>(R.id.btnCitasEmpleado).setOnClickListener { mostrarCitas() }
        findViewById<LinearLayout>(R.id.btnMedicosEmpleado).setOnClickListener { mostrarMedicos() }
        findViewById<Button>(R.id.btnRegistrarCitaDesdeInicioEmpleado).setOnClickListener { formularioCita() }
        cargarResumen()
    }

    private fun regresar() = findViewById<TextView>(R.id.btnRegresarEmpleado).setOnClickListener { mostrarInicio() }

    private fun mostrarPacientes() {
        abrir(R.layout.activity_pacientes_empleado, 1)
        regresar()
        findViewById<Button>(R.id.btnRegistrarPacienteEmpleado).setOnClickListener { formularioPaciente(null) }
        val buscar = findViewById<EditText>(R.id.txtBuscarPacienteEmpleado)
        buscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                cargarPacientes(s?.toString().orEmpty())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
        cargarPacientes("")
    }

    private fun cargarPacientes(texto: String) {
        solicitudPacientes?.cancel()
        solicitudPacientes = scope.launch {
            try {
                delay(220)
                val pacientes = presentador.buscarPacientes(texto)
                if (pantalla == 1) mostrarLista(pacientes)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                if (pantalla == 1) {
                    findViewById<TextView>(R.id.txtCantidadPacientesEmpleado).text = "Error al consultar"
                    Toast.makeText(this@menuEmpleado, e.message ?: "Error de conexión", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun mostrarLista(pacientes: List<dataPacienteEmpleado>) {
        val lista = findViewById<LinearLayout>(R.id.contenedorPacientesEmpleado)
        lista.removeAllViews()
        findViewById<TextView>(R.id.txtCantidadPacientesEmpleado).text = "${pacientes.size} resultados"
        for (paciente in pacientes) {
            val fila = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = android.view.Gravity.CENTER_VERTICAL
                setPadding(dp(12), dp(12), dp(12), dp(12))
                background = GradientDrawable().apply {
                    setColor(Color.WHITE)
                    cornerRadius = dp(15).toFloat()
                    setStroke(dp(1), Color.parseColor("#DFEAF1"))
                }
            }
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            params.bottomMargin = dp(10)
            val datos = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
            val p = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            datos.addView(TextView(this).apply {
                text = "◉  ${paciente.nombre} ${paciente.apellidos}"
                textSize = 15f
                setTextColor(Color.parseColor("#273F50"))
                setTypeface(null, Typeface.BOLD)
            })
            datos.addView(TextView(this).apply {
                text = "     Tel. ${paciente.telefono}"
                textSize = 12f
                setTextColor(Color.parseColor("#80919F"))
            })
            fila.addView(datos, p)
            fila.addView(Button(this).apply {
                text = "Actualizar"
                isAllCaps = false
                textSize = 11f
                minHeight = 40
                setTextColor(Color.parseColor("#2F80B7"))
                backgroundTintList = ColorStateList.valueOf(Color.parseColor("#E7F3FB"))
                setOnClickListener { formularioPaciente(paciente) }
            })
            lista.addView(fila, params)
        }
        if (pacientes.isEmpty()) lista.addView(TextView(this).apply {
            text = "No se encontraron pacientes"
            textSize = 14f
            setTextColor(Color.parseColor("#7A8F9E"))
        })
    }

    private fun formularioPaciente(paciente: dataPacienteEmpleado?) {
        val contenedor = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(20), dp(4), dp(20), dp(4))
        }
        fun campo(etiqueta: String, valor: String): EditText {
            return EditText(this).apply {
                hint = etiqueta
                setSingleLine(true)
                setText(valor)
                contenedor.addView(this)
            }
        }
        val nombre = campo("Nombre", paciente?.nombre.orEmpty())
        val apellidos = campo("Apellidos", paciente?.apellidos.orEmpty())
        val correo = campo("Correo electrónico", paciente?.correo.orEmpty()).apply {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        }
        val telefono = campo("Teléfono (10 dígitos)", paciente?.telefono.orEmpty()).apply {
            inputType = InputType.TYPE_CLASS_PHONE
        }
        val curp = campo("CURP (opcional)", paciente?.curp.orEmpty())
        val nacimiento = campo("Fecha de nacimiento (AAAA-MM-DD)", paciente?.fechaNacimiento.orEmpty())
        val nuevaClave = if (paciente == null) campo("Contraseña (mínimo 8 caracteres)", "").apply {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        } else null
        val scroll = ScrollView(this).apply { addView(contenedor) }
        val dialogo = AlertDialog.Builder(this)
            .setTitle(if (paciente == null) "Registrar paciente" else "Actualizar paciente")
            .setView(scroll)
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Guardar", null)
            .create()
        dialogo.setOnShowListener {
            val boton = dialogo.getButton(AlertDialog.BUTTON_POSITIVE)
            boton.setOnClickListener {
                boton.isEnabled = false
                scope.launch {
                    val error = presentador.guardarPaciente(
                        paciente, nombre.text.toString(), apellidos.text.toString(),
                        correo.text.toString(), telefono.text.toString(),
                        curp.text.toString(), nacimiento.text.toString(), nuevaClave?.text?.toString().orEmpty()
                    )
                    boton.isEnabled = true
                    if (error != null) {
                        Toast.makeText(this@menuEmpleado, error, Toast.LENGTH_LONG).show()
                    } else {
                        dialogo.dismiss()
                        Toast.makeText(this@menuEmpleado, if (paciente == null) "Paciente registrado" else "Datos actualizados", Toast.LENGTH_SHORT).show()
                        cargarPacientes(findViewById<EditText>(R.id.txtBuscarPacienteEmpleado).text.toString())
                    }
                }
            }
        }
        dialogo.show()
    }


    // Datos para las tarjetas del inicio
    private fun cargarResumen() {
        scope.launch {
            try {
                val pacientes = agenda.pacientes()
                if (pantalla == 0) findViewById<TextView>(R.id.txtTotalPacientesEmpleado).text = pacientes.size.toString()
            } catch (_: Exception) { /* Se mantiene la información anterior */ }
            try {
                val citas = agenda.citas()
                val medicos = agenda.medicos()
                if (pantalla == 0) {
                    findViewById<TextView>(R.id.txtResumenCitasEmpleado).text =
                        citas.count { it.estado == "pendiente" || it.estado == "confirmada" }.toString()
                    findViewById<TextView>(R.id.txtResumenMedicosEmpleado).text = medicos.size.toString()
                }
            } catch (e: Exception) {
                if (pantalla == 0) aviso(e)
            }
            try {
                val respuesta = com.example.citasmed.Modelo.Retrofit.apiservice.obtenerEspecialidades()
                val json = respuesta.body() as? kotlinx.serialization.json.JsonArray
                if (pantalla == 0 && respuesta.isSuccessful && json != null) {
                    findViewById<TextView>(R.id.txtResumenEspecialidadesEmpleado).text = json.size.toString()
                }
            } catch (_: Exception) { /* Si falla, se puede seguir usando el menú */ }
        }
    }

    private fun mostrarCitas() {
        abrir(R.layout.activity_citas_empleado, 2)
        regresar()
        findViewById<Button>(R.id.btnRegistrarCitaEmpleado).setOnClickListener { formularioCita() }
        val buscar = findViewById<EditText>(R.id.txtBuscarCitaEmpleado)
        buscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                cargarCitas(s?.toString().orEmpty())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
        cargarCitas("")
    }

    private fun cargarCitas(texto: String) {
        solicitudCitas?.cancel()
        solicitudCitas = scope.launch {
            try {
                delay(200)
                val resultado = agenda.citas(texto)
                if (pantalla == 2) pintarCitas(resultado)
            } catch (e: CancellationException) { throw e }
              catch (e: Exception) {
                if (pantalla == 2) {
                    findViewById<TextView>(R.id.txtEstadoCitasEmpleado).text = "No se pudieron cargar las citas"
                    findViewById<LinearLayout>(R.id.contenedorCitasEmpleado).removeAllViews()
                    aviso(e)
                }
            }
        }
    }

    private fun pintarCitas(citas: List<dataCitaEmpleado>) {
        val contenedor = findViewById<LinearLayout>(R.id.contenedorCitasEmpleado)
        contenedor.removeAllViews()
        findViewById<TextView>(R.id.txtEstadoCitasEmpleado).text = "${citas.size} citas encontradas"
        for (cita in citas) {
            val tarjeta = tarjeta()
            tarjeta.addView(etiqueta("${cita.paciente} · ${cita.medico}", true))
            tarjeta.addView(etiqueta("${cita.especialidad} | ${cita.fecha} ${cita.hora.take(5)}"))
            tarjeta.addView(etiqueta("Estado: ${cita.estado} · ${cita.motivo}"))
            if (cita.estado == "pendiente" || cita.estado == "confirmada") {
                val botones = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
                botones.addView(boton("Reprogramar") { reprogramarCita(cita) },
                    LinearLayout.LayoutParams(0, dp(44), 1f))
                botones.addView(boton("Cancelar") { confirmarCancelacion(cita) },
                    LinearLayout.LayoutParams(0, dp(44), 1f))
                tarjeta.addView(botones)
            }
            contenedor.addView(tarjeta, separacion())
        }
        if (citas.isEmpty()) contenedor.addView(etiqueta("No se encontraron citas"))
    }

    private fun mostrarMedicos() {
        abrir(R.layout.activity_medicos_empleado, 3)
        regresar()
        val buscar = findViewById<EditText>(R.id.txtBuscarMedicoEmpleado)
        buscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (pantalla == 3) pintarMedicos(s?.toString().orEmpty())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
        solicitudMedicos?.cancel()
        solicitudMedicos = scope.launch {
            try {
                listaMedicos = agenda.medicos()
                if (pantalla == 3) pintarMedicos(buscar.text.toString())
            } catch (e: CancellationException) { throw e }
              catch (e: Exception) {
                if (pantalla == 3) {
                    findViewById<TextView>(R.id.txtEstadoMedicosEmpleado).text = "No se pudieron cargar los médicos"
                    findViewById<LinearLayout>(R.id.contenedorMedicosEmpleado).removeAllViews()
                    aviso(e)
                }
            }
        }
    }

    private fun pintarMedicos(busqueda: String) {
        val encontrados = listaMedicos.filter {
            (it.nombreCompleto + " " + it.especialidad).contains(busqueda.trim(), ignoreCase = true)
        }
        val contenedor = findViewById<LinearLayout>(R.id.contenedorMedicosEmpleado)
        contenedor.removeAllViews()
        findViewById<TextView>(R.id.txtEstadoMedicosEmpleado).text = "${encontrados.size} médicos encontrados"
        for (medico in encontrados) {
            val tarjeta = tarjeta()
            tarjeta.addView(etiqueta(medico.nombreCompleto, true))
            tarjeta.addView(etiqueta("${medico.especialidad} · Consultorio: ${medico.consultorio}"))
            tarjeta.addView(boton("Consultar horarios disponibles") { mostrarHorarios(medico) })
            contenedor.addView(tarjeta, separacion())
        }
        if (encontrados.isEmpty()) contenedor.addView(etiqueta("No se encontraron médicos"))
    }

    private fun mostrarHorarios(medico: dataMedicoEmpleado) {
        scope.launch {
            try {
                val horarios = agenda.horarios(medico.id)
                val contenido = horarios.take(50).joinToString("\n") { it.etiqueta }
                    .ifBlank { "No hay horarios disponibles" }
                AlertDialog.Builder(this@menuEmpleado)
                    .setTitle("Horarios de ${medico.nombreCompleto}")
                    .setMessage(contenido)
                    .setPositiveButton("Cerrar", null).show()
            } catch (e: Exception) { aviso(e) }
        }
    }

    private fun formularioCita() {
        scope.launch {
            try {
                val pacientes = agenda.pacientes()
                val medicos = agenda.medicos()
                if (pacientes.isEmpty() || medicos.isEmpty()) {
                    Toast.makeText(this@menuEmpleado,
                        "Necesitas al menos un paciente y un médico", Toast.LENGTH_LONG).show()
                    return@launch
                }
                val formulario = LinearLayout(this@menuEmpleado).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(dp(16), dp(10), dp(16), dp(10))
                }
                formulario.addView(etiqueta("Paciente"))
                val pacienteSpinner = Spinner(this@menuEmpleado)
                pacienteSpinner.adapter = ArrayAdapter(this@menuEmpleado,
                    android.R.layout.simple_spinner_dropdown_item,
                    pacientes.map { "${it.nombre} ${it.apellidos}" })
                formulario.addView(pacienteSpinner)
                formulario.addView(etiqueta("Médico"))
                val medicoSpinner = Spinner(this@menuEmpleado)
                medicoSpinner.adapter = ArrayAdapter(this@menuEmpleado,
                    android.R.layout.simple_spinner_dropdown_item,
                    medicos.map { "${it.nombreCompleto} · ${it.especialidad}" })
                formulario.addView(medicoSpinner)
                val horarioTexto = etiqueta("Horario: sin seleccionar")
                val botonHorario = boton("Seleccionar horario") {}
                var horarioElegido: dataHorarioEmpleado? = null
                var idMedicoElegido = -1
                medicoSpinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) {
                        horarioElegido = null
                        idMedicoElegido = -1
                        horarioTexto.text = "Horario: sin seleccionar"
                    }
                    override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
                }
                botonHorario.setOnClickListener {
                    val medico = medicos[medicoSpinner.selectedItemPosition]
                    elegirHorario(medico.id) { horario ->
                        horarioElegido = horario
                        idMedicoElegido = medico.id
                        horarioTexto.text = "Horario: ${horario.etiqueta}"
                    }
                }
                formulario.addView(botonHorario)
                formulario.addView(horarioTexto)
                val motivo = EditText(this@menuEmpleado).apply { hint = "Motivo de la cita" }
                formulario.addView(motivo)
                val scroll = ScrollView(this@menuEmpleado).apply { addView(formulario) }
                val dialogo = AlertDialog.Builder(this@menuEmpleado)
                    .setTitle("Registrar cita")
                    .setView(scroll)
                    .setNegativeButton("Salir", null)
                    .setPositiveButton("Guardar cita", null).create()
                dialogo.setOnShowListener {
                    val guardar = dialogo.getButton(AlertDialog.BUTTON_POSITIVE)
                    guardar.setOnClickListener {
                        val medico = medicos[medicoSpinner.selectedItemPosition]
                        val h = horarioElegido
                        if (h == null || idMedicoElegido != medico.id) {
                            Toast.makeText(this@menuEmpleado, "Selecciona un horario del médico", Toast.LENGTH_LONG).show()
                            return@setOnClickListener
                        }
                        guardar.isEnabled = false
                        scope.launch {
                            try {
                                agenda.registrar(pacientes[pacienteSpinner.selectedItemPosition].id,
                                    medico.id, h, motivo.text.toString())
                                dialogo.dismiss()
                                Toast.makeText(this@menuEmpleado, "Cita registrada", Toast.LENGTH_SHORT).show()
                                if (pantalla == 2) cargarCitas("") else if (pantalla == 0) cargarResumen()
                            } catch (e: Exception) { aviso(e) }
                            finally { guardar.isEnabled = true }
                        }
                    }
                }
                dialogo.show()
            } catch (e: CancellationException) { throw e }
              catch (e: Exception) { aviso(e) }
        }
    }

    private fun elegirHorario(idMedico: Int, seleccionado: (dataHorarioEmpleado) -> Unit) {
        scope.launch {
            try {
                val horarios = agenda.horarios(idMedico)
                if (horarios.isEmpty()) {
                    Toast.makeText(this@menuEmpleado, "No hay horarios disponibles para este médico", Toast.LENGTH_LONG).show()
                    return@launch
                }
                AlertDialog.Builder(this@menuEmpleado)
                    .setTitle("Seleccionar fecha y hora")
                    .setItems(horarios.map { it.etiqueta }.toTypedArray()) { _, pos ->
                        seleccionado(horarios[pos])
                    }.setNegativeButton("Cancelar", null).show()
            } catch (e: Exception) { aviso(e) }
        }
    }

    private fun reprogramarCita(cita: dataCitaEmpleado) {
        elegirHorario(cita.idMedico) { horario ->
            AlertDialog.Builder(this)
                .setTitle("Confirmar reprogramación")
                .setMessage("Cambiar la cita de ${cita.paciente} a ${horario.etiqueta}?")
                .setNegativeButton("No", null)
                .setPositiveButton("Sí") { _, _ ->
                    scope.launch {
                        try {
                            agenda.reprogramar(cita, horario)
                            Toast.makeText(this@menuEmpleado, "Cita reprogramada", Toast.LENGTH_SHORT).show()
                            if (pantalla == 2) cargarCitas("")
                        } catch (e: Exception) { aviso(e) }
                    }
                }.show()
        }
    }

    private fun confirmarCancelacion(cita: dataCitaEmpleado) {
        AlertDialog.Builder(this)
            .setTitle("Cancelar cita")
            .setMessage("¿Cancelar la cita de ${cita.paciente} del ${cita.fecha}?")
            .setNegativeButton("No", null)
            .setPositiveButton("Sí, cancelar") { _, _ ->
                scope.launch {
                    try {
                        agenda.cancelar(cita)
                        Toast.makeText(this@menuEmpleado, "Cita cancelada", Toast.LENGTH_SHORT).show()
                        if (pantalla == 2) cargarCitas("")
                    } catch (e: Exception) { aviso(e) }
                }
            }.show()
    }

    private fun aviso(error: Throwable) {
        Toast.makeText(this, error.message ?: "No se pudo consultar el servidor", Toast.LENGTH_LONG).show()
    }

    private fun tarjeta() = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(dp(14), dp(12), dp(14), dp(12))
        background = GradientDrawable().apply {
            setColor(Color.WHITE)
            cornerRadius = dp(12).toFloat()
            setStroke(dp(1), Color.parseColor("#DFEAF1"))
        }
    }
    private fun separacion() = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply { bottomMargin = dp(10) }
    private fun etiqueta(texto: String, destacado: Boolean = false) = TextView(this).apply {
        text = texto
        textSize = if (destacado) 15f else 13f
        setTextColor(Color.parseColor(if (destacado) "#273F50" else "#688398"))
        if (destacado) setTypeface(null, Typeface.BOLD)
        setPadding(dp(4), dp(4), dp(4), dp(8))
    }
    private fun boton(titulo: String, accion: () -> Unit) = Button(this).apply {
        text = titulo
        isAllCaps = false
        textSize = 12f
        setTextColor(Color.parseColor("#2F80B7"))
        backgroundTintList = ColorStateList.valueOf(Color.parseColor("#E7F3FB"))
        setOnClickListener { accion() }
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
