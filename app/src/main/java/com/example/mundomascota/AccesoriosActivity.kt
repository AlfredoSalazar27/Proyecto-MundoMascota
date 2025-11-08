
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


        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }


        findViewById<MaterialButton>(R.id.btnComprarCollar).setOnClickListener {

        }
        findViewById<MaterialButton>(R.id.btnComprarBola).setOnClickListener {

        }
        findViewById<MaterialButton>(R.id.btnComprarHueso).setOnClickListener {

        }
        findViewById<MaterialButton>(R.id.btnComprarCama).setOnClickListener {

        }
    }
}