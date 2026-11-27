package com.example.citasmed.vista

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
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.setPadding
import com.example.citasmed.Modelo.clsModelo
import com.example.citasmed.Modelo.dataEspecialidadHorariosAdmin
import com.example.citasmed.Modelo.dataMedicoAdmin
import com.example.citasmed.Presentador.clsPresentadorAdmin
import com.example.citasmed.R
import kotlin.time.Duration

class menuAdministrador : AppCompatActivity() {

    private val presentadorAdmin = clsPresentadorAdmin()

    private var pantalla = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mostrarInicio()
    }
    private fun abrir(layout: Int, seccion: Int) {
        pantalla = seccion
        setContentView(layout)
        findViewById<LinearLayout>(R.id.navInicioAdmin).setOnClickListener { mostrarInicio() }
        findViewById<LinearLayout>(R.id.navMedicosAdmin).setOnClickListener { mostrarMedicos() }
        findViewById<LinearLayout>(R.id.navEspecialidadesAdmin).setOnClickListener { mostrarEspecHorarios() }
        findViewById<LinearLayout>(R.id.navUsuariosAdmin).setOnClickListener { mostrarUsuarios() }

        val nombres = intArrayOf(R.id.labelNavInicioAdmin, R.id.labelNavMedicosAdmin,
            R.id.labelNavEspecialidadesAdmin, R.id.labelNavUsuariosAdmin)
        val iconos = intArrayOf(R.id.iconNavInicioAdmin, R.id.iconNavMedicosAdmin,
            R.id.iconNavEspecialidadesAdmin, R.id.iconNavUsuarioAdmin)
        for (i in 0..2) {
            val color =
                if (i == seccion) Color.parseColor("#2F80B7") else Color.parseColor("#8796A4")
            findViewById<TextView>(nombres[i]).setTextColor(color)
            findViewById<TextView>(iconos[i]).setTextColor(color)
        }
        val nombre = clsModelo.sesion?.nombre.orEmpty().trim()

    }
    private fun mostrarInicio(){
        abrir(R.layout.activity_menu_administrador,  0)
        findViewById<LinearLayout>(R.id.btnMedicosAdmin).setOnClickListener { mostrarMedicos() }
        findViewById<LinearLayout>(R.id.btnEspecialidaades_horarios).setOnClickListener { mostrarEspecHorarios() }
        findViewById<LinearLayout>(R.id.btnUsuarios_permisos).setOnClickListener { (mostrarUsuarios()) }
    }
    private fun regresar() = findViewById<TextView>(R.id.btnRegresarAdmin).setOnClickListener { mostrarInicio() }

    private fun mostrarMedicos() {
        abrir(R.layout.activity_gestion_medicos_admin, 1)
        regresar()
        val buscar = findViewById<EditText>(R.id.txtBuscarMedicoEspec)
        val tarjetas = intArrayOf(R.id.tarjetaMedicoAdmin1, R.id.tarjetaMedicoAdmin2, R.id.tarjetaMedicoAdmin3)
        val nombres = arrayOf("elena ramírez medicina general", "luis méndez cardiología", "carlos sánchez pediatría")
        buscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int
            ) {}
            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                val busqueda = s?.toString()?.trim().orEmpty()
                for (i in tarjetas.indices) {
                    val tarjeta = findViewById<View>(tarjetas[i])
                    val coincide = busqueda.isEmpty() || nombres[i].contains(busqueda, ignoreCase = true)
                    tarjeta.visibility = if (coincide) View.VISIBLE else View.GONE
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }
    private fun mostrarListaEspecHorarios(){
        val lista = findViewById<LinearLayout>(R.id.contenedorEspecialidadesHorarios)
        lista.removeAllViews()
        val registros = presentadorAdmin.obtenerEspecialidades()
        for (registros in registros) {
            val fila = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = android.view.Gravity.CENTER_VERTICAL
                setPadding((12), (12), (12), (12))
                background = GradientDrawable().apply {
                    setColor(Color.WHITE)
                    cornerRadius =  (15).toFloat()
                    setStroke((1), Color.parseColor("#DFEAF1"))
                }
            }

            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            params.bottomMargin = (10)

            val datos = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
            val p = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)

            datos.addView(TextView(this).apply {
                text = "◉  ${registros.nombre}"
                textSize = 15f
                setTextColor(Color.parseColor("#273F50"))
                setTypeface(null, Typeface.BOLD)
            })

            datos.addView(TextView(this).apply {
                text = "     ${registros.descripcion}"
                textSize = 12f
                setTextColor(Color.parseColor("#80919F"))
            })
            fila.addView(datos, p)
            val boton = Button(this).apply {
                text = "Actualizar"
                isAllCaps = false
                textSize = 11f
                minHeight = 40
                setTextColor(Color.parseColor("#2F80B7"))
                backgroundTintList = ColorStateList.valueOf(Color.parseColor("#E7F3FB"))
                setOnClickListener { formularioEspecHorario(registros) }
            }

            fila.addView(boton)
            lista.addView(fila, params)
        }

        if (registros.isEmpty()) {
            lista.addView(TextView(this).apply {
                text = "No se encontraron especialidades o horarios"
                textSize = 14f
                setTextColor(Color.parseColor("#7A8F9E"))
            })
        }
    }

    private fun mostrarEspecHorarios(){
        abrir(layout = R.layout.activity_menu_especialidades_horarios_admin, seccion = 2)
        regresar()
        findViewById<Button>(R.id.btnAgregarInfo).setOnClickListener {
            formularioEspecHorario(null)
        }
        mostrarListaEspecHorarios()
    }
    private fun formularioEspecHorario(especHorario: dataEspecialidadHorariosAdmin?) {
        val contenedor = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding((20), (5), (20), 0)
        }
        val nombre = EditText(this).apply {
            hint= "nombre de la espcecialidad"
            setSingleLine(true)
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
            setText(especHorario?.nombre.orEmpty())
        }
        val descripcion = EditText(this). apply {
            hint = "Descripcion de la especialidad"
            setSingleLine(true)
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
            setText(especHorario?.descripcion.orEmpty())
        }
        contenedor.addView(nombre)
        contenedor.addView(descripcion)
        val dialogo = AlertDialog.Builder(this)
            .setTitle(if (especHorario == null) "Registrar la especilidad" else "Actualizar especialidad")
            .setView(contenedor)
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Guardar", null)
            .create()
        dialogo.setOnShowListener {
            dialogo.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val error = presentadorAdmin.guardarEspecHorarios(especHorario?.id, nombre.text.toString(), descripcion.text.toString())
                if (error != null) Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                else {
                    dialogo.dismiss()
                    Toast.makeText(this, "Guardado en demostración (no MySQL)", Toast.LENGTH_SHORT).show()
                }
            }
        }
        dialogo.show()
    }
    private fun formularioMedico(medico: dataMedicoAdmin?) {
        val contenedor = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding((20), (5), (20), 0)
        }

        val nombre = EditText(this).apply {
            hint = "Nombre del médico"
            setSingleLine(true)
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
            setText(medico?.nombre.orEmpty())
        }

        val especialidad = EditText(this).apply {
            hint = "Especialidad"
            setSingleLine(true)
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
            setText(medico?.especialidad.orEmpty())
        }

        val horario = EditText(this).apply {
            hint = "Horario (ej. Lun a Vie · 9:00-14:00)"
            setSingleLine(true)
            inputType = InputType.TYPE_CLASS_TEXT
            setText(medico?.horario.orEmpty())
        }

        contenedor.addView(nombre)
        contenedor.addView(especialidad)
        contenedor.addView(horario)

        val dialogo = AlertDialog.Builder(this)
            .setTitle(if (medico == null) "Registrar médico" else "Actualizar médico")
            .setView(contenedor)
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Guardar", null)
            .create()

        dialogo.setOnShowListener {
            dialogo.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val error = presentadorAdmin.guardarMedico(
                    medico?.id,
                    nombre.text.toString(),
                    especialidad.text.toString(),
                    horario.text.toString()
                )
                if (error != null) {
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                } else {
                    dialogo.dismiss()
                    Toast.makeText(this, "Guardado correctamente", Toast.LENGTH_SHORT).show()
                }
            }
        }

        dialogo.show()
    }
    private fun mostrarUsuarios(){
        abrir(layout = R.layout.activity_usuarios_permisos_admin, 3)
        regresar()

        val tarjetas = intArrayOf(R.id.tarjetaPaciente, R.id.tarjetaEmpleado, R.id.tarjetaMedico, R.id.tarjetaAdmin)
        tarjetas.forEach { idTarjeta ->
            findViewById<View>(idTarjeta).setOnClickListener {
                when (idTarjeta) {
                    R.id.tarjetaPaciente -> abrirPermisosRol("Paciente")
                    R.id.tarjetaEmpleado -> abrirPermisosRol("Empleado")
                    R.id.tarjetaMedico -> abrirPermisosRol("Medico")
                    R.id.tarjetaAdmin -> abrirPermisosRol("Administrador")
                }
            }
        }
    }
    private fun abrirPermisosRol(rol: String) {
        AlertDialog.Builder(this)
            .setTitle("Permisos: $rol")
            .setMessage("Configurar permisos para el perfil $rol")
            .setPositiveButton("Aceptar", null)
            .show()
    }
}