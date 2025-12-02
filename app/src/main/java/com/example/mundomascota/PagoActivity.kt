// PagoActivity.kt
package com.example.mundomascota

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PagoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pago)

        // === RECIBIR DATOS DEL PRODUCTO ===
        val productName = intent.getStringExtra("PRODUCT_NAME") ?: "Producto"
        val productPrice = intent.getDoubleExtra("PRODUCT_PRICE", 0.0)
        val productImageRes = intent.getIntExtra("PRODUCT_IMAGE", R.drawable.ic_pet_placeholder)

        // === REFERENCIAS A VISTAS ===
        val ivProductImage = findViewById<ImageView>(R.id.iv_product_image)
        val tvProductName = findViewById<TextView>(R.id.tv_product_name)
        val tvProductPrice = findViewById<TextView>(R.id.tv_product_price)
        val btnConfirmPayment = findViewById<Button>(R.id.btn_confirm_payment)
        val btnBack = findViewById<ImageButton>(R.id.btnBack) // FLECHA

        // === MOSTRAR DATOS DEL PRODUCTO ===
        ivProductImage.setImageResource(productImageRes)
        tvProductName.text = productName
        tvProductPrice.text = "₡ ${String.format("%.2f", productPrice)}"

        // === FLECHA IZQUIERDA → VOLVER A MENU ===
        btnBack.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        // === BOTÓN CONFIRMAR PAGO ===
        btnConfirmPayment.setOnClickListener {
            // Deshabilitar botón y cambiar texto
            btnConfirmPayment.apply {
                isEnabled = false
                text = "Pagado"
                setBackgroundColor(resources.getColor(android.R.color.darker_gray, theme))
            }

            // Toast de confirmación
            Toast.makeText(this, "¡Gracias por comprar $productName!", Toast.LENGTH_LONG).show()

            // Ir a comprobante o cerrar después de 2 segundos
            btnConfirmPayment.postDelayed({
                // Opción 1: Ir a Comprobante
                startActivity(Intent(this, ComprobanteActivity::class.java))
                finish()

                // Opción 2: Solo cerrar (descomenta si prefieres)
                // finish()
            }, 2000)
        }
    }
}