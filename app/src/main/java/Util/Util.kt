// Util.kt
package com.example.mundomascota

import java.text.NumberFormat
import java.util.*

object Util {
    fun formatearMoneda(valor: Double): String {
        val formato = NumberFormat.getCurrencyInstance(Locale("es", "CR"))
        formato.maximumFractionDigits = 0
        return formato.format(valor)
    }
}