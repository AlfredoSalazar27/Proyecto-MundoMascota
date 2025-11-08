
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


        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }


        findViewById<MaterialButton>(R.id.btnComprarVacuna).setOnClickListener {

        }
        findViewById<MaterialButton>(R.id.btnComprarAlimento).setOnClickListener {

        }
        findViewById<MaterialButton>(R.id.btnComprarMedicina).setOnClickListener {

        }
        findViewById<MaterialButton>(R.id.btnComprarPastillas).setOnClickListener {

        }
    }
}