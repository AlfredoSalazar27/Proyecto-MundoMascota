package com.example.mundomascota

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class AccesoriosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accesorios)


        val btnMenu = findViewById<MaterialButton>(R.id.btnMenu)
        btnMenu.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish()
        }


        setupComprarButtons()
    }

    private fun setupComprarButtons() {
        findViewById<MaterialButton>(R.id.btnComprarCollar).setOnClickListener {
            showToast("Collar agregado al carrito")
        }
        findViewById<MaterialButton>(R.id.btnComprarBola).setOnClickListener {
            showToast("Bola de estambre agregada al carrito")
        }
        findViewById<MaterialButton>(R.id.btnComprarHueso).setOnClickListener {
            showToast("Hueso agregado al carrito")
        }
        findViewById<MaterialButton>(R.id.btnComprarColcha).setOnClickListener {
            showToast("Colcha agregada al carrito")
        }
    }

    private fun showToast(msg: String) {
        android.widget.Toast.makeText(this, msg, android.widget.Toast.LENGTH_SHORT).show()
    }
}