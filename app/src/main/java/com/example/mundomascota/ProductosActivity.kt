package com.example.mundomascota

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class ProductosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)


        val btnMenu = findViewById<MaterialButton>(R.id.btnMenu)
        btnMenu.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish()
        }


        setupComprarButtons()
    }

    private fun setupComprarButtons() {
        findViewById<MaterialButton>(R.id.btnComprarVacuna).setOnClickListener {
            showToast("Vacuna agregada al carrito")
        }
        findViewById<MaterialButton>(R.id.btnComprarAlimento).setOnClickListener {
            showToast("Alimento agregado al carrito")
        }
        findViewById<MaterialButton>(R.id.btnComprarMedicina).setOnClickListener {
            showToast("Medicina agregada al carrito")
        }
        findViewById<MaterialButton>(R.id.btnComprarPastillas).setOnClickListener {
            showToast("Pastillas agregadas al carrito")
        }
    }

    private fun showToast(msg: String) {
        android.widget.Toast.makeText(this, msg, android.widget.Toast.LENGTH_SHORT).show()
    }
}