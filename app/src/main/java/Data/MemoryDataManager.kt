package Data

import Entity.*

object MemoryDataManager : IDataManager {
    private val usuarios = mutableListOf<Usuario>()
    private val productos = mutableListOf<Producto>()
    private val itemsCarrito = mutableListOf<ItemCarrito>()

    override fun add(entity: Any) {
        when (entity) {
            is Usuario -> usuarios.add(entity)
            is Producto -> productos.add(entity)
            is ItemCarrito -> itemsCarrito.add(entity)
            else -> throw IllegalArgumentException("Tipo de entidad no soportado")
        }
    }

    override fun update(entity: Any) {
        when (entity) {
            is Usuario -> {
                remove(entity.Id)
                add(entity)
            }
            is Producto -> {
                remove(entity.Id)
                add(entity)
            }
            is ItemCarrito -> {
                remove(entity.Producto.Id)
                add(entity)
            }
            else -> throw IllegalArgumentException("Tipo de entidad no soportado")
        }
    }

    override fun remove(id: String) {
        usuarios.removeIf { it.Id.trim() == id.trim() }
        productos.removeIf { it.Id.trim() == id.trim() }
        itemsCarrito.removeIf { it.Producto.Id.trim() == id.trim() }
    }

    override fun getAll(): List<Any> {
        return usuarios + productos + itemsCarrito
    }

    override fun getById(id: String): Any? {
        val usuario = usuarios.find { it.Id.trim() == id.trim() }
        if (usuario != null) return usuario
        val producto = productos.find { it.Id.trim() == id.trim() }
        if (producto != null) return producto
        val itemCarrito = itemsCarrito.find { it.Producto.Id.trim() == id.trim() }
        return itemCarrito
    }

    override fun getUsuarioByNombreUsuario(nombreUsuario: String): Usuario? {
        return usuarios.find { it.NombreUsuario.trim() == nombreUsuario.trim() }
    }

    override fun getProductosPorCategoria(categoria: Categoria): List<Producto> {
        val result = productos.filter { it.Categoria == categoria }
        return if (result.isNotEmpty()) result else emptyList()
    }

    override fun getItemsCarrito(): List<ItemCarrito> {
        return itemsCarrito.toList()
    }

    override fun clearCarrito() {
        itemsCarrito.clear()
    }
}