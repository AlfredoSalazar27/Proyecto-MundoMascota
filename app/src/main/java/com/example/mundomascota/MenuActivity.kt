
package com.example.mundomascota

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)


        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }


        findViewById<MaterialButton>(R.id.btnPerros).setOnClickListener {
            startActivity(Intent(this, PerrosActivity::class.java))
        }

        findViewById<MaterialButton>(R.id.btnGatos).setOnClickListener {
            startActivity(Intent(this, GatosActivity::class.java))
        }

        findViewById<MaterialButton>(R.id.btnAccesorios).setOnClickListener {
            startActivity(Intent(this, AccesoriosActivity::class.java))
        }

        findViewById<MaterialButton>(R.id.btnProductos).setOnClickListener {
            startActivity(Intent(this, ProductosActivity::class.java))
        }

    }
}