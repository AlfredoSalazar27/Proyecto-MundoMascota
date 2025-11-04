package com.example.mundomascota

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class PerrosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perros)


        val btnMenu = findViewById<MaterialButton>(R.id.btnMenu)


        btnMenu.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish() // Cierra PerrosActivity para no acumular pantallas
        }



        setupComprarButtons()
    }

    private fun setupComprarButtons() {
        val btnComprarFirulais = findViewById<MaterialButton>(R.id.btnComprarFirulais)
        val btnComprarOscar = findViewById<MaterialButton>(R.id.btnComprarOscar)
        val btnComprarDraco = findViewById<MaterialButton>(R.id.btnComprarDraco)
        val btnComprarColochini = findViewById<MaterialButton>(R.id.btnComprarColochini)

        btnComprarFirulais.setOnClickListener { mostrarToast("Firulais agregado al carrito") }
        btnComprarOscar.setOnClickListener { mostrarToast("Oscar agregado al carrito") }
        btnComprarDraco.setOnClickListener { mostrarToast("Draco agregado al carrito") }
        btnComprarColochini.setOnClickListener { mostrarToast("Colochini agregado al carrito") }
    }

    private fun mostrarToast(mensaje: String) {
        android.widget.Toast.makeText(this, mensaje, android.widget.Toast.LENGTH_SHORT).show()
    }



}