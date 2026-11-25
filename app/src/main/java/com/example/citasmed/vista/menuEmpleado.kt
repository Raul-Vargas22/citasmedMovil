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
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.citasmed.R
import com.example.citasmed.Modelo.clsModelo
import com.example.citasmed.Modelo.dataPacienteEmpleado
import com.example.citasmed.Presentador.clsPresentadorEmpleado

class menuEmpleado : AppCompatActivity() {
    private val presentador = clsPresentadorEmpleado()
    private var pantalla = 0
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var solicitudPacientes: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mostrarInicio()
    }

    private fun abrir(layout: Int, seccion: Int) {
        if (seccion != 1) solicitudPacientes?.cancel()
        pantalla = seccion
        setContentView(layout)
        findViewById<TextView>(R.id.txtCerrarSesionEmpleado).setOnClickListener { finish() }
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
        val nombre = clsModelo.sesion?.nombre.orEmpty().trim()
        val iniciales = nombre.split(" ").filter { it.isNotBlank() }.take(2)
            .joinToString("") { it.take(1).uppercase() }
        if (iniciales.isNotBlank()) findViewById<TextView>(R.id.txtInicialesEmpleado).text = iniciales
    }

    private fun mostrarInicio() {
        abrir(R.layout.activity_menu_empleado, 0)
        findViewById<LinearLayout>(R.id.btnPacientesEmpleado).setOnClickListener { mostrarPacientes() }
        findViewById<LinearLayout>(R.id.btnCitasEmpleado).setOnClickListener { mostrarCitas() }
        findViewById<LinearLayout>(R.id.btnMedicosEmpleado).setOnClickListener { mostrarMedicos() }
        findViewById<Button>(R.id.btnRegistrarCitaDesdeInicioEmpleado).setOnClickListener { mostrarCitas() }
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
        val nacimiento = campo("Nacimiento AAAA-MM-DD (opcional)", paciente?.fechaNacimiento.orEmpty())
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
                        curp.text.toString(), nacimiento.text.toString()
                    )
                    boton.isEnabled = true
                    if (error != null) {
                        Toast.makeText(this@menuEmpleado, error, Toast.LENGTH_LONG).show()
                    } else {
                        dialogo.dismiss()
                        Toast.makeText(this@menuEmpleado, "Guardado en MySQL", Toast.LENGTH_SHORT).show()
                        cargarPacientes(findViewById<EditText>(R.id.txtBuscarPacienteEmpleado).text.toString())
                    }
                }
            }
        }
        dialogo.show()
    }

    private fun mostrarCitas() {
        abrir(R.layout.activity_citas_empleado, 2)
        regresar()
        val pendiente = View.OnClickListener { Toast.makeText(this, "Función pendiente de conectar con la API", Toast.LENGTH_SHORT).show() }
        findViewById<Button>(R.id.btnRegistrarCitaEmpleado).setOnClickListener(pendiente)
        findViewById<Button>(R.id.btnReprogramarCita1).setOnClickListener(pendiente)
        findViewById<Button>(R.id.btnCancelarCita1).setOnClickListener(pendiente)
        findViewById<Button>(R.id.btnReprogramarCita2).setOnClickListener(pendiente)
        findViewById<Button>(R.id.btnCancelarCita2).setOnClickListener(pendiente)
    }

    private fun mostrarMedicos() {
        abrir(R.layout.activity_medicos_empleado, 3)
        regresar()
        val buscar = findViewById<EditText>(R.id.txtBuscarMedicoEmpleado)
        val tarjetas = intArrayOf(R.id.tarjetaMedico1, R.id.tarjetaMedico2, R.id.tarjetaMedico3)
        val nombres = arrayOf("elena ramírez medicina general", "luis méndez cardiología", "carlos sánchez pediatría")
        buscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val busqueda = s?.toString()?.trim().orEmpty()
                for (i in tarjetas.indices) {
                    // Hide the entire card including its outside margins.
                    val interior = findViewById<View>(tarjetas[i])
                    (interior.parent as View).visibility = if (nombres[i].contains(busqueda, ignoreCase = true)) View.VISIBLE else View.GONE
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (pantalla != 0) mostrarInicio() else super.onBackPressed()
    }
}
