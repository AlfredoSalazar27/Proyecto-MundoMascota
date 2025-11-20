package com.example.mundomascota

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class PerrosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perros)

        // Botón de volver atrás
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
            finish()
        }

        // === BOTONES DE COMPRA ===

        // Firulais - €85.000
        findViewById<MaterialButton>(R.id.btnComprarFirulais).setOnClickListener {
            abrirPago("Firulais", 85000.0, R.drawable.firulais)
        }

        // Oscar - €72.500
        findViewById<MaterialButton>(R.id.btnComprarOscar).setOnClickListener {
            abrirPago("Oscar", 72500.0, R.drawable.oscar)
        }

        // Draco - €95.000
        findViewById<MaterialButton>(R.id.btnComprarDraco).setOnClickListener {
            abrirPago("Draco", 95000.0, R.drawable.draco)
        }

        // Colochini - €68.000
        findViewById<MaterialButton>(R.id.btnComprarColochini).setOnClickListener {
            abrirPago("Colochini", 68000.0, R.drawable.colochini)
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