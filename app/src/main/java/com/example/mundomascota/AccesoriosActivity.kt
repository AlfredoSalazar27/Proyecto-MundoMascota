package com.example.mundomascota

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class AccesoriosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accesorios)

        // Botón de volver atrás
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
            finish()
        }

        // === BOTONES DE COMPRA ===

        // Collar - €2.500
        findViewById<MaterialButton>(R.id.btnComprarCollar).setOnClickListener {
            abrirPago("Collar", 2500.0, R.drawable.collar)
        }

        // Bola Estambre - €1.700
        findViewById<MaterialButton>(R.id.btnComprarBola).setOnClickListener {
            abrirPago("Bola Estambre", 1700.0, R.drawable.estambre)
        }

        // Hueso - €5.200
        findViewById<MaterialButton>(R.id.btnComprarHueso).setOnClickListener {
            abrirPago("Hueso", 5200.0, R.drawable.hueso)
        }

        // Colcha - €6.250
        findViewById<MaterialButton>(R.id.btnComprarCama).setOnClickListener {
            abrirPago("Colcha", 6250.0, R.drawable.colcha)
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