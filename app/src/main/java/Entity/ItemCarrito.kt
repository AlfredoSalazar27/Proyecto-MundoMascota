
package Entity

import com.example.mundomascota.Util

class ItemCarrito(
    var Producto: Producto,
    var Cantidad: Int = 1
) {
    fun obtenerSubtotal(): Double = Producto.Precio * Cantidad

    fun obtenerSubtotalFormateado(): String = Util.formatearMoneda(obtenerSubtotal())
}