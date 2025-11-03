package com.example.mundomascota  // Cambia por tu paquete real

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class GatosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gatos)


        val btnMenu = findViewById<MaterialButton>(R.id.btnMenu)
        btnMenu.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish() // Cierra GatosActivity para no acumular pantallas
        }


        setupComprarButtons()
    }

    private fun setupComprarButtons() {
        findViewById<MaterialButton>(R.id.btnComprarMissingo).setOnClickListener {
            mostrarMensaje("Missingo agregado al carrito")
        }
        findViewById<MaterialButton>(R.id.btnComprarJorge).setOnClickListener {
            mostrarMensaje("Jorge agregado al carrito")
        }
        findViewById<MaterialButton>(R.id.btnComprarOjitos).setOnClickListener {
            mostrarMensaje("Ojitos agregado al carrito")
        }
        findViewById<MaterialButton>(R.id.btnComprarPelussa).setOnClickListener {
            mostrarMensaje("Pelussa agregado al carrito")
        }
    }

    private fun mostrarMensaje(mensaje: String) {
        android.widget.Toast.makeText(this, mensaje, android.widget.Toast.LENGTH_SHORT).show()
    }



}