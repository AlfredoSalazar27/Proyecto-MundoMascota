package Entity

import Entity.Producto

class ItemCarrito {
    private var producto: Producto = Producto("", "", 0.0, "", Categoria.PERROS)
    private var cantidad: Int = 1

    constructor(producto: Producto, cantidad: Int) {
        this.producto = producto
        this.cantidad = cantidad
    }

    var Producto: Producto
        get() = this.producto
        set(value) { this.producto = value }

    var Cantidad: Int
        get() = this.cantidad
        set(value) { this.cantidad = value }

    fun obtenerSubtotal(): Double = producto.Precio * cantidad
    fun obtenerSubtotalFormateado(): String = Util.formatearMoneda(obtenerSubtotal())
}