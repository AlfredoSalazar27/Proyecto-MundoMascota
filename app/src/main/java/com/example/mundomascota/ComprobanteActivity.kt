
package com.example.mundomascota

import Controller.ControladorItemCarrito
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class ComprobanteActivity : AppCompatActivity() {

    private lateinit var controlador: ControladorItemCarrito

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comprobante)

        controlador = ControladorItemCarrito(this)

        findViewById<MaterialButton>(R.id.btnSalir).setOnClickListener {

            controlador.limpiarCarrito()

            startActivity(Intent(this, MenuActivity::class.java))
            finishAffinity()
        }
    }
}