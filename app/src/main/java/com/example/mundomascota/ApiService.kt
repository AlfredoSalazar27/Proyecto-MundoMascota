package com.example.mundomascota.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class UsuarioRequest(
    val nombre: String,
    val correo: String,
    val fotoUrl: String? = null,
    val telefono: String
)

data class CompraRequest(
    val usuarioId: String,
    val productos: String,
    val telefono: Int,
    val total: Double
)

data class ApiResponse(
    val mensaje: String
)

interface ApiService {
    @POST("usuarios")
    suspend fun registrarUsuario(@Body usuario: UsuarioRequest): Response<ApiResponse>

    @POST("compras")
    suspend fun guardarCompra(@Body compra: CompraRequest): Response<ApiResponse>

    @GET("compras")
    suspend fun obtenerHistorial(): Response<List<CompraRequest>>
}