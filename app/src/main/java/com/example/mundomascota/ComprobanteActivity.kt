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

        // === VER HISTORIAL SIN SALIR ===
        findViewById<MaterialButton>(R.id.btnVerHistorial).setOnClickListener {
            // GUARDAR LA COMPRA ACTUAL EN HISTORIAL
            controlador.guardarCompraEnHistorial()

            // ABRIR HISTORIAL (NO SE CIERRA LA APP)
            startActivity(Intent(this, HistorialActivity::class.java))
        }

        // === SALIR (GUARDA Y CIERRA) ===
        findViewById<MaterialButton>(R.id.btnSalir).setOnClickListener {
            // GUARDAR HISTORIAL
            controlador.guardarCompraEnHistorial()

            // LIMPIAR CARRITO
            controlador.limpiarCarrito()

            // IR A DESPEDIDA Y CERRAR TODO
            val intent = Intent(this, DespedidaActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finishAffinity()
        }
    }
}