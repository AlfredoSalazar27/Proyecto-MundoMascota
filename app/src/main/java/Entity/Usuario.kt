package Entity

import java.sql.Date

class Usuario {
    private var id: String = ""
    private var nombreUsuario: String = ""
    private var contrasena: String = ""

    constructor(id: String, nombreUsuario: String, contrasena: String) {
        this.id = id
        this.nombreUsuario = nombreUsuario
        this.contrasena = contrasena
    }

    var Id: String
        get() = this.id
        set(value) { this.id = value }

    var NombreUsuario: String
        get() = this.nombreUsuario
        set(value) { this.nombreUsuario = value }

    var Contrasena: String
        get() = this.contrasena
        set(value) { this.contrasena = value }

    fun validar(nombreUsuarioIngresado: String, contrasenaIngresada: String): Boolean {
        return nombreUsuario == nombreUsuarioIngresado && contrasena == contrasenaIngresada
    }
}