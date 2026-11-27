
package com.example.citasmed.vista

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import com.example.citasmed.Modelo.clsModelo
import com.example.citasmed.R
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class clsNavegacionPaciente(private val actividad: Activity) {

    fun configurarNavegacion() {

        val contenido = actividad.findViewById<ViewGroup>(android.R.id.content).getChildAt(0)

        val paddingSuperior = contenido.paddingTop

        ViewCompat.setOnApplyWindowInsetsListener(contenido) { vista, insets ->
            val altoBarra = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            vista.setPadding(vista.paddingLeft, paddingSuperior + altoBarra, vista.paddingRight, vista.paddingBottom)
            insets
        }

        ViewCompat.requestApplyInsets(contenido)

        val titulo = when (actividad) {
            is menuPaciente -> "Inicio"
            is nuevaCitaPaciente -> "Nueva cita"
            is misCitasPaciente -> "Mis citas"
            is medicosPaciente -> "Médicos"
            else -> "Paciente"
        }

        actividad.findViewById<TextView>(R.id.txtTituloPantallaPaciente)?.text = titulo

        val txtIniciales = actividad.findViewById<TextView>(R.id.txtInicialesPaciente)

        val nombre = clsModelo.sesion?.nombre ?: ""
        val apellidos = clsModelo.sesion?.apellidos ?: ""

        val iniciales = (nombre.trim().firstOrNull()?.uppercaseChar()?.toString() ?: "") + (apellidos.trim().firstOrNull()?.uppercaseChar()?.toString() ?: "")

        txtIniciales?.text = iniciales
        val navInicio = actividad.findViewById<LinearLayout>(R.id.navInicioPaciente)
        val navNuevaCita = actividad.findViewById<LinearLayout>(R.id.navNuevaCitaPaciente)
        val navMisCitas = actividad.findViewById<LinearLayout>(R.id.navMisCitasPaciente)
        val navMedicos = actividad.findViewById<LinearLayout>(R.id.navMedicosPaciente)

        when (actividad) {
            is menuPaciente -> actualizarSeleccion(navInicio)
            is nuevaCitaPaciente -> actualizarSeleccion(navNuevaCita)
            is misCitasPaciente -> actualizarSeleccion(navMisCitas)
            is medicosPaciente -> actualizarSeleccion(navMedicos)
        }
        navInicio?.setOnClickListener {
            abrirPantalla(menuPaciente::class.java)
        }

        navNuevaCita?.setOnClickListener {
            abrirPantalla(nuevaCitaPaciente::class.java)
        }

        navMisCitas?.setOnClickListener {
            abrirPantalla(misCitasPaciente::class.java)
        }

        navMedicos?.setOnClickListener {
            abrirPantalla(medicosPaciente::class.java)
        }
        val cerrarSesion = actividad.findViewById<TextView>(R.id.txtCerrarSesionPaciente)
        cerrarSesion?.setOnClickListener {
            clsModelo.sesion = null
            val intent = Intent(actividad, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            actividad.startActivity(intent)
        }
    }

    private fun actualizarSeleccion(seleccionado: LinearLayout?) {

        val opciones = listOf(R.id.navInicioPaciente to Pair(R.id.navInicioIcono, R.id.navInicioTexto), R.id.navNuevaCitaPaciente to Pair(R.id.navNuevaCitaIcono, R.id.navNuevaCitaTexto), R.id.navMisCitasPaciente to Pair(R.id.navMisCitasIcono, R.id.navMisCitasTexto), R.id.navMedicosPaciente to Pair(R.id.navMedicosIcono, R.id.navMedicosTexto))

        val azul = Color.rgb(40, 120, 200)
        val gris = Color.rgb(119, 119, 119)

        for ((idOpcion, idsTextos) in opciones) {

            val opcion = actividad.findViewById<LinearLayout>(idOpcion)
            val icono = actividad.findViewById<TextView>(idsTextos.first)
            val texto = actividad.findViewById<TextView>(idsTextos.second)
            val estaSeleccionada = opcion == seleccionado
            val color = if (estaSeleccionada) azul else gris
            icono?.setTextColor(color)
            texto?.setTextColor(color)
            opcion?.animate()?.cancel()
            opcion?.scaleX = 1f
            opcion?.scaleY = 1f

            if (estaSeleccionada) {
                opcion?.animate() ?.scaleX(1.08f) ?.scaleY(1.08f) ?.setDuration(220) ?.setInterpolator(OvershootInterpolator()) ?.start()
            }
        }
    }

    private fun abrirPantalla(destino: Class<*>) {

        if (actividad::class.java == destino) return

        val intent = Intent(actividad, destino)
        actividad.startActivity(intent)
    }
}
