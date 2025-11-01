package Entity

import Entity.Categoria

class Producto {
    private var id: String = ""
    private var nombre: String = ""
    private var precio: Double = 0.0
    private var rutaImagen: String = ""
    private var categoria: Categoria = Categoria

    constructor(id: String, nombre: String, precio: Double, rutaImagen: String, categoria: Categoria) {
        this.id = id
        this.nombre = nombre
        this.precio = precio
        this.rutaImagen = rutaImagen
        this.categoria = categoria
    }

    var Id: String
        get() = this.id
        set(value) { this.id = value }

    var Nombre: String
        get() = this.nombre
        set(value) { this.nombre = value }

    var Precio: Double
        get() = this.precio
        set(value) { this.precio = value }

    var RutaImagen: String
        get() = this.rutaImagen
        set(value) { this.rutaImagen = value }

    var Categoria: Categoria
        get() = this.categoria
        set(value) { this.categoria = value }

    fun obtenerPrecioFormateado(): String = Util.formatearMoneda(precio)
}