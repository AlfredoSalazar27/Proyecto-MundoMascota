package Data

import Entity.*

interface IDataManager {
    fun add(entity: Any)
    fun update(entity: Any)
    fun remove(id: String)
    fun getAll(): List<Any>
    fun getById(id: String): Any?
    fun getUsuarioByNombreUsuario(nombreUsuario: String): Usuario?
    fun getProductosPorCategoria(categoria: Categoria): List<Producto>
    fun getItemsCarrito(): List<ItemCarrito>
    fun clearCarrito()
}