package Controller

import Entity.Compra
import Entity.ItemCarrito
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*

class ControladorItemCarrito(private val context: Context) {

    private val prefs = context.getSharedPreferences("carrito_actual", Context.MODE_PRIVATE)
    private val gson = Gson()

    // === CARRITO ACTUAL (GUARDADO EN DISCO) ===
    fun obtenerTodosLosItems(): List<ItemCarrito> {
        val json = prefs.getString("items_carrito", null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<ItemCarrito>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun agregarItemCarrito(item: ItemCarrito) {
        val items = obtenerTodosLosItems().toMutableList()
        val existente = items.find { it.Producto.Id == item.Producto.Id }
        if (existente != null) {
            existente.Cantidad += item.Cantidad
        } else {
            items.add(item)
        }
        guardarCarrito(items)
    }

    fun actualizarItemCarrito(item: ItemCarrito) {
        val items = obtenerTodosLosItems().toMutableList()
        items.removeAll { it.Producto.Id == item.Producto.Id }
        items.add(item)
        guardarCarrito(items)
    }

    fun eliminarItemCarrito(idProducto: String) {
        val items = obtenerTodosLosItems().toMutableList()
        items.removeAll { it.Producto.Id == idProducto }
        guardarCarrito(items)
    }

    fun limpiarCarrito() {
        prefs.edit().remove("items_carrito").apply()
    }

    private fun guardarCarrito(items: List<ItemCarrito>) {
        prefs.edit().putString("items_carrito", gson.toJson(items)).apply()
    }

    fun obtenerTotalCompra(): Double = obtenerTodosLosItems().sumOf { it.obtenerSubtotal() }

    // === HISTORIAL DE COMPRAS (GUARDADO EN DISCO) ===
    fun guardarCompraEnHistorial() {
        val items = obtenerTodosLosItems()
        if (items.isEmpty()) return

        val productos = items.map { "${it.Producto.Nombre} x${it.Cantidad}" }.joinToString(", ")
        val total = obtenerTotalCompra()
        val fecha = SimpleDateFormat("dd MMM yyyy - HH:mm", Locale("es", "CR")).format(Date())

        val nuevaCompra = Compra(fecha, productos, "₡ ${String.format("%.0f", total)}")

        val prefsHistorial = context.getSharedPreferences("historial_app", Context.MODE_PRIVATE)
        val listaActual = obtenerHistorialCompras().toMutableList()
        listaActual.add(0, nuevaCompra)

        prefsHistorial.edit()
            .putString("historial", gson.toJson(listaActual.take(20)))
            .apply()
    }

    fun obtenerHistorialCompras(): List<Compra> {
        val prefs = context.getSharedPreferences("historial_app", Context.MODE_PRIVATE)
        val json = prefs.getString("historial", null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<Compra>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}