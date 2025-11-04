
package com.example.mundomascota

import Controller.ControladorItemCarrito
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class DespedidaActivity : AppCompatActivity() {

    private lateinit var controlador: ControladorItemCarrito

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_despedida)



        controlador = ControladorItemCarrito(this)


        findViewById<MaterialButton>(R.id.btnVolverInicio).setOnClickListener {

            controlador.limpiarCarrito()


            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)


            finish()
        }
    }
}