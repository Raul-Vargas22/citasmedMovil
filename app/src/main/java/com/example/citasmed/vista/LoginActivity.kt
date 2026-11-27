package com.example.citasmed.vista

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.citasmed.Presentador.clsPresentadorLogin
import com.example.citasmed.R

class LoginActivity : AppCompatActivity() {

    private lateinit var presentador: clsPresentadorLogin

    private lateinit var txtCorreo: EditText
    private lateinit var txtContrasena: EditText
    private lateinit var btnIniciarSesion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

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

        txtCorreo = findViewById(R.id.txtCorreo)
        txtContrasena = findViewById(R.id.txtContrasena)
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion)

        presentador = clsPresentadorLogin(this)

        btnIniciarSesion.setOnClickListener {

            val correo = txtCorreo.text.toString().trim()
            val contrasena = txtContrasena.text.toString().trim()

            presentador.realizarLogin(correo, contrasena)
        }
    }

    fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    fun loginExitoso(mensaje: String, nombre: String) {
        Toast.makeText(
            this,
            "$mensaje\nBienvenido $nombre",
            Toast.LENGTH_SHORT
        ).show()
    }
}