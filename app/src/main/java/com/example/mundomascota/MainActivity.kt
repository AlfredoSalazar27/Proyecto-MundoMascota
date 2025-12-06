
package com.example.mundomascota

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // === HUELLA QUE GIRA SUAVEMENTE ===
        val ivLogo = findViewById<ImageView>(R.id.ivLogo)
        val rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_paw)
        ivLogo.startAnimation(rotateAnimation)

        // BOTÓN ACEPTAR (LOGIN)
        findViewById<MaterialButton>(R.id.btnAceptar).setOnClickListener {
            val usuario = findViewById<TextInputEditText>(R.id.txtUsuario).text.toString().trim()
            val password = findViewById<TextInputEditText>(R.id.txtPassword).text.toString()

            if (usuario == "admin" && password == "1234") {
                Toast.makeText(this, "¡Bienvenido!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MenuActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_LONG).show()
            }
        }

        // BOTÓN CANCELAR
        findViewById<MaterialButton>(R.id.btnCancelar).setOnClickListener {
            finish()
        }

        // BOTÓN REGISTRARSE
        findViewById<android.widget.TextView>(R.id.tvRegistrarse).setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }
    }
}