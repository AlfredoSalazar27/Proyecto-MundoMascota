package com.example.mundomascota

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class ProductosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)

        // Botón de volver atrás
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
            finish()
        }

        // === BOTONES DE COMPRA ===

        // Vacuna - €12.900
        findViewById<MaterialButton>(R.id.btnComprarVacuna).setOnClickListener {
            abrirPago("Vacuna", 12900.0, R.drawable.vacuna)
        }

        // Alimento - €6.000
        findViewById<MaterialButton>(R.id.btnComprarAlimento).setOnClickListener {
            abrirPago("Alimento", 6000.0, R.drawable.alimento)
        }

        // Medicina - €21.400
        findViewById<MaterialButton>(R.id.btnComprarMedicina).setOnClickListener {
            abrirPago("Medicina", 21400.0, R.drawable.medicina)
        }

        // Pastillas - €3.000
        findViewById<MaterialButton>(R.id.btnComprarPastillas).setOnClickListener {
            abrirPago("Pastillas", 3000.0, R.drawable.pastilla)
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