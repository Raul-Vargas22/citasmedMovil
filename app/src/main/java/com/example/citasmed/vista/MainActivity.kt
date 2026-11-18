package com.example.citasmed.vista

import android.os.Bundle
import android.view.View
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.citasmed.R
import com.example.citasmed.Presentador.clsPresentador

class MainActivity : AppCompatActivity() {

    private lateinit var txtUsuario: EditText
    private lateinit var txtContra: EditText
    private lateinit var btnInicioSesion: Button
    private lateinit var btnSalir: Button
    private lateinit var opcPerfiles: Spinner

    private lateinit var presentador: clsPresentador

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        if (android.os.Build.VERSION.SDK_INT >= 37) {
            val permiso = "android.permission.ACCESS_LOCAL_NETWORK"

            if (checkSelfPermission(permiso) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(permiso), 100)
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        txtUsuario = findViewById(R.id.txtUsuario)
        txtContra = findViewById(R.id.txtContra)
        btnSalir = findViewById(R.id.btnSalir)
        btnInicioSesion = findViewById(R.id.btnInicioSesion)
        opcPerfiles = findViewById(R.id.opcPerfiles)

        presentador = clsPresentador(this)

        btnInicioSesion.setOnClickListener(this::ejecutarInicioSesion)

        btnSalir.setOnClickListener {
            finish()
        }
    }

    fun ejecutarInicioSesion(v: View) {
        val usuario = txtUsuario.text.toString()
        val contra = txtContra.text.toString()
        val perfil = opcPerfiles.selectedItem.toString()

        if (usuario.isBlank() || contra.isBlank()) {
            mostrarMensaje("Ingresa usuario y contraseña")
            return
        }

        presentador.inicioSesion(usuario, contra, perfil)
    }

    fun abrirVentana(perfil: String) {
        val destino = when (perfil) {
            "Administrador" -> menuAdministrador::class.java
            "Doctor" -> menuDoctor::class.java
            "Empleado" -> menuEmpleado::class.java
            else -> {
                mostrarMensaje("Selecciona un perfil válido")
                return
            }
        }

        startActivity(Intent(this, destino))
    }

    fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}