package com.example.citasmed.vista
import com.example.citasmed.Modelo.clsModelo
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.citasmed.Presentador.clsPresentadorPaciente
import com.example.citasmed.R
import android.content.Intent

class menuPaciente : AppCompatActivity() {
    private lateinit var txtCerrarSesionPaciente: TextView
    private lateinit var txtInicialesPaciente: TextView
    private lateinit var txtBienvenidaPaciente: TextView
    private lateinit var btnNuevaCitaPaciente: Button
    private lateinit var btnConsultarCita: Button
    private lateinit var btnFuncionNuevaCita: Button
    private lateinit var btnFuncionMisCitas: Button
    private lateinit var btnFuncionMedicos: Button

    private lateinit var navInicioPaciente: LinearLayout
    private lateinit var navNuevaCitaPaciente: LinearLayout
    private lateinit var navMisCitasPaciente: LinearLayout
    private lateinit var navMedicosPaciente: LinearLayout
    private lateinit var presentador: clsPresentadorPaciente
    private lateinit var navegacion: clsNavegacionPaciente

    override fun onCreate(saveInstanceState: Bundle?){
        super.onCreate(saveInstanceState)
        setContentView(R.layout.activity_menu_paciente)
        presentador= clsPresentadorPaciente(this)
        txtCerrarSesionPaciente = findViewById(R.id.txtCerrarSesionPaciente)
        txtInicialesPaciente=findViewById(R.id.txtInicialesPaciente)
        txtBienvenidaPaciente=findViewById(R.id.txtBienvenidaPaciente)
        txtBienvenidaPaciente.text = "Bienvenido, ${clsModelo.sesion?.nombre}"
        btnNuevaCitaPaciente=findViewById(R.id.btnNuevaCitaPaciente)
        btnConsultarCita=findViewById(R.id.btnConsultarCita)
        btnFuncionNuevaCita=findViewById(R.id.btnFuncionNuevaCita)
        btnFuncionMisCitas= findViewById(R.id.btnFuncionMisCitas)
        btnFuncionMedicos=findViewById(R.id.btnFuncionMedicos)

        navInicioPaciente=findViewById(R.id.navInicioPaciente)
        navNuevaCitaPaciente=findViewById(R.id.navNuevaCitaPaciente)
        navMisCitasPaciente=findViewById(R.id.navMisCitasPaciente)
        navMedicosPaciente=findViewById(R.id.navMedicosPaciente)

        btnNuevaCitaPaciente.setOnClickListener(this::ejecutarAccionNuevaCita)
        btnConsultarCita.setOnClickListener(this::ejecutarAccionConsultarCita)
        btnFuncionNuevaCita.setOnClickListener(this::ejecutarAccionNuevaCita)
        btnFuncionMisCitas.setOnClickListener(this::ejecutarAccionMisCitas)
        btnFuncionMedicos.setOnClickListener(this::ejecutarAccionMedicos)
        navegacion= clsNavegacionPaciente(this)
        navegacion.configurarNavegacion()
    }
    private fun ejecutarAccionInicio(v: View) {

    }
    private fun ejecutarAccionNuevaCita(v: View) {
        val destino = Intent(this, nuevaCitaPaciente::class.java)
        startActivity(destino)
    }

    private fun ejecutarAccionConsultarCita(v: View) {
        presentador.consultarCita()
    }

    private fun ejecutarAccionMisCitas(v: View) {
        presentador.mostrarMisCitas()
    }
    fun abrirMisCitas() {
        val destino = Intent(this, misCitasPaciente::class.java)
        startActivity(destino)
    }
    fun abrirMedicos() {
        val destino = Intent(this, medicosPaciente::class.java)
        startActivity(destino)
    }

    private fun ejecutarAccionMedicos(v: View) {
        presentador.mostrarMedicos()
    }

}