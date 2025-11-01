package Controller

import Data.IDataManager
import Data.MemoryDataManager
import Entity.ItemCarrito
import Entity.Producto
import android.content.Context
import com.example.mundomascota.R

class ControladorItemCarrito {
    private var dataManager: IDataManager = MemoryDataManager
    private lateinit var context: Context

    constructor(context: Context) {
        this.context = context
    }

    fun obtenerItemCarritoPorId(idProducto: String): ItemCarrito {
        try {
            val result = dataManager.getById(idProducto) as? ItemCarrito
            if (result == null) {
                throw Exception(context.getString(R.string.MsgDataNoFound))
            }
            return result
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.ErrorMsgGetById))
        }
    }

    fun agregarItemCarrito(item: ItemCarrito) {
        try {
            dataManager.add(item)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.ErrorMsgAdd))
        }
    }

    fun actualizarItemCarrito(item: ItemCarrito) {
        try {
            dataManager.update(item)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.ErrorMsgUpdate))
        }
    }

    fun eliminarItemCarrito(item: ItemCarrito) {
        try {
            dataManager.remove(item.Producto.Id)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.ErrorMsgRemove))
        }
    }

    fun limpiarCarrito() {
        try {
            dataManager.clearCarrito()
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.ErrorMsgRemove))
        }
    }
}