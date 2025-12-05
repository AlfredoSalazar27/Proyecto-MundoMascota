// DespedidaActivity.kt
package com.example.mundomascota

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class DespedidaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_despedida)

        // VOLVER AL INICIO
        findViewById<MaterialButton>(R.id.btnVolverInicio).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // CERRAR SESIÓN → CIERRA LA APP COMPLETAMENTE
        findViewById<MaterialButton>(R.id.btnCerrarSesion).setOnClickListener {
            finishAffinity() // Cierra TODAS las actividades
            System.exit(0)    // Cierra la app 100%
        }
    }
}