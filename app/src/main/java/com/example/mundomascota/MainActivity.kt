package com.example.mundomascota

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private lateinit var txtUsuario: TextInputEditText
    private lateinit var txtPassword: TextInputEditText
    private lateinit var btnAceptar: MaterialButton
    private lateinit var btnCancelar: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        txtUsuario = findViewById(R.id.txtUsuario)
        txtPassword = findViewById(R.id.txtPassword)
        btnAceptar = findViewById(R.id.btnAceptar)
        btnCancelar = findViewById(R.id.btnCancelar)

        val ivPaw = findViewById<ImageView>(R.id.ivPaw)

        val rotation = android.animation.ObjectAnimator.ofFloat(ivPaw, "rotation", 0f, 360f)
        rotation.duration = 1000
        rotation.repeatCount = 1
        rotation.start()


        btnAceptar.setOnClickListener {
            val usuario = txtUsuario.text.toString().trim()
            val password = txtPassword.text.toString()

            if (usuario == "admin" && password == "1234") {
                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
                finish() // Cierra el login
            } else {
                android.widget.Toast.makeText(
                    this,
                    "Usuario o contraseña incorrectos",
                    android.widget.Toast.LENGTH_LONG
                ).show()
            }
        }


        btnCancelar.setOnClickListener {
            txtUsuario.text?.clear()
            txtPassword.text?.clear()
            txtUsuario.requestFocus()
        }
    }
}