package com.example.mundomascota

import Controller.ControladorItemCarrito
import Entity.Compra
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistorialActivity : AppCompatActivity() {

    private lateinit var rvHistorial: RecyclerView
    private lateinit var adapter: HistorialAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial)

        rvHistorial = findViewById(R.id.rvHistorial)
        rvHistorial.layoutManager = LinearLayoutManager(this)

        val compras = ControladorItemCarrito(this).obtenerHistorialCompras()

        findViewById<TextView>(R.id.tvEmpty).visibility =
            if (compras.isEmpty()) View.VISIBLE else View.GONE

        adapter = HistorialAdapter(compras)
        rvHistorial.adapter = adapter

        findViewById<ImageButton>(R.id.btnBack)?.setOnClickListener {
            finish()
        }
    }
}