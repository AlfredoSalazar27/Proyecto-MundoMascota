package Entity

import java.text.NumberFormat
import java.util.Locale

class Util {
    companion object {
        fun formatearMoneda(cantidad: Double): String {
            val formateador = NumberFormat.getNumberInstance(Locale("es", "CO"))
            return "$ ${formateador.format(cantidad)}"
        }
    }
}