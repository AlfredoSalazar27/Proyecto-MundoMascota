package com.example.mundomascota

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)


        val btnPerros = findViewById<MaterialButton>(R.id.btnPerros)
        val btnGatos = findViewById<MaterialButton>(R.id.btnGatos)
        val btnAccesorios = findViewById<MaterialButton>(R.id.btnAccesorios)
        val btnProductos = findViewById<MaterialButton>(R.id.btnProductos)


        btnPerros.setOnClickListener {
            val intent = Intent(this, PerrosActivity::class.java)
            startActivity(intent)
        }


        btnGatos.setOnClickListener {
            val intent = Intent(this, GatosActivity::class.java)
            startActivity(intent)
        }

        btnAccesorios.setOnClickListener {
            val intent = Intent(this, AccesoriosActivity::class.java)
            startActivity(intent)
        }

        btnProductos.setOnClickListener {
            val intent = Intent(this, ProductosActivity::class.java)
            startActivity(intent)
        }

    }
}