package Controller

import Data.IDataManager
import Data.MemoryDataManager
import Entity.Producto
import Entity.Categoria
import android.content.Context
import com.example.mundomascota.R

class ControladorProducto {
    private var dataManager: IDataManager = MemoryDataManager
    private lateinit var Context: Context

    constructor(context: Context) {
        this.Context = context
    }

    fun obtenerProductoPorId(id: String): Producto {
        try {
            val result = dataManager.getById(id) as? Producto
            if (result == null) {
                throw Exception(Context.getString(R.string.MsgDataNoFound))
            }
            return result
        } catch (e: Exception) {
            throw Exception(Context.getString(R.string.ErrorMsgGetById))
        }
    }

    fun agregarProducto(producto: Producto) {
        try {
            dataManager.add(producto)
        } catch (e: Exception) {
            throw Exception(Context.getString(R.string.ErrorMsgAdd))
        }
    }

    fun actualizarProducto(producto: Producto) {
        try {
            dataManager.update(producto)
        } catch (e: Exception) {
            throw Exception(Context.getString(R.string.ErrorMsgUpdate))
        }
    }

    fun obtenerProductosPorCategoria(categoria: Categoria): List<Producto> {
        try {
            val resultados = dataManager.getProductosPorCategoria(categoria)
            if (resultados.isEmpty()) {
                throw Exception(Context.getString(R.string.MsgDataNoFound))
            }
            return resultados
        } catch (e: Exception) {
            throw Exception(Context.getString(R.string.ErrorMsgGetById))
        }
    }
}