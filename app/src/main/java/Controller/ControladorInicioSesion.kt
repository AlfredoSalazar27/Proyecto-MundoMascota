package Controller

import Data.IDataManager
import Data.MemoryDataManager
import Entity.Usuario
import android.content.Context
import com.example.mundomascota.R

class ControladorInicioSesion {
    private var dataManager: IDataManager = MemoryDataManager
    private lateinit var Context: Context

    constructor(context: Context) {
        this.Context = context
    }

    fun iniciarSesion(nombreUsuario: String, contrasena: String): Usuario {
        try {
            val usuario = dataManager.getUsuarioByNombreUsuario(nombreUsuario)
            if (usuario == null || !usuario.validar(nombreUsuario, contrasena)) {
                throw Exception(Context.getString(R.string.MsgDataNoFound))
            }
            return usuario
        } catch (e: Exception) {
            throw Exception(Context.getString(R.string.ErrorMsgGetById))
        }
    }

    fun agregarUsuario(usuario: Usuario) {
        try {
            dataManager.add(usuario)
        } catch (e: Exception) {
            throw Exception(Context.getString(R.string.ErrorMsgAdd))
        }
    }

    fun actualizarUsuario(usuario: Usuario) {
        try {
            dataManager.update(usuario)
        } catch (e: Exception) {
            throw Exception(Context.getString(R.string.ErrorMsgUpdate))
        }
    }
}