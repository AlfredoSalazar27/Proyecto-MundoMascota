package com.example.mundomascota

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class GatosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gatos)

        // Botón de volver atrás
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
            finish()
        }

        // === BOTONES DE COMPRA ===

        // Missingo - €78.000
        findViewById<MaterialButton>(R.id.btnComprarMissingo).setOnClickListener {
            abrirPago("Missingo", 78000.0, R.drawable.azucena)
        }

        // Jorge - €92.500
        findViewById<MaterialButton>(R.id.btnComprarJorge).setOnClickListener {
            abrirPago("Jorge", 92500.0, R.drawable.jorge)
        }

        // Ojitos - €65.000
        findViewById<MaterialButton>(R.id.btnComprarOjitos).setOnClickListener {
            abrirPago("Ojitos", 65000.0, R.drawable.jude)
        }

        // Pelussa - €88.000
        findViewById<MaterialButton>(R.id.btnComprarPelussa).setOnClickListener {
            abrirPago("Pelussa", 88000.0, R.drawable.macho)
        }
    }

    // Función reutilizable para abrir PagoActivity
    private fun abrirPago(nombre: String, precio: Double, imagenRes: Int) {
        val intent = Intent(this, PagoActivity::class.java).apply {
            putExtra("PRODUCT_NAME", nombre)
            putExtra("PRODUCT_PRICE", precio)
            putExtra("PRODUCT_IMAGE", imagenRes)
        }
        startActivity(intent)
    }
}