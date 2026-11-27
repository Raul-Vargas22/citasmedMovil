package com.example.citasmed.vista

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.citasmed.R

class medicosPaciente : AppCompatActivity() {

    private lateinit var txtBuscarMedico: EditText
    private lateinit var txtRegresarMedicos: android.widget.TextView
    private lateinit var navegacion: clsNavegacionPaciente

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicos_paciente)

        txtBuscarMedico = findViewById(R.id.txtBuscarMedico)
        txtRegresarMedicos = findViewById(R.id.txtRegresarMedicos)
        txtRegresarMedicos.setOnClickListener(this::ejecutarRegresar)
        navegacion = clsNavegacionPaciente(this)
        navegacion.configurarNavegacion()
    }

    private fun ejecutarRegresar(v: View) {
        finish()
    }


}
