// ComprobanteActivity.kt
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

        // Inicializar controlador del carrito
        controlador = ControladorItemCarrito(this)

        // BOTÓN SALIR → VA A DESPEDIDA
        findViewById<MaterialButton>(R.id.btnSalir).setOnClickListener {
            // 1. Limpiar el carrito
            controlador.limpiarCarrito()

            // 2. Ir a DespedidaActivity
            val intent = Intent(this, DespedidaActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

            // 3. Cerrar TODAS las actividades anteriores
            finishAffinity()
        }
    }
}