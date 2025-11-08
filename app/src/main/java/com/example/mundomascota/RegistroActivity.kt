
package com.example.mundomascota

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import android.widget.EditText
import android.widget.ImageView
import java.util.*

class RegistroActivity : AppCompatActivity() {

    private lateinit var etId: EditText
    private lateinit var etNombre: EditText
    private lateinit var etPrimerApellido: EditText
    private lateinit var etSegundoApellido: EditText
    private lateinit var etFechaNacimiento: EditText
    private lateinit var etCorreo: EditText
    private lateinit var etTelefono: EditText
    private lateinit var etProvincia: EditText
    private lateinit var etCanton: EditText
    private lateinit var etDistrito: EditText
    private lateinit var etDireccion: EditText

    private val prefs by lazy { getSharedPreferences("RegistroUsuario", Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)


        etId = findViewById(R.id.etId)
        etNombre = findViewById(R.id.etNombre)
        etPrimerApellido = findViewById(R.id.etPrimerApellido)
        etSegundoApellido = findViewById(R.id.etSegundoApellido)
        etFechaNacimiento = findViewById(R.id.etFechaNacimiento)
        etCorreo = findViewById(R.id.etCorreo)
        etTelefono = findViewById(R.id.etTelefono)
        etProvincia = findViewById(R.id.etProvincia)
        etCanton = findViewById(R.id.etCanton)
        etDistrito = findViewById(R.id.etDistrito)
        etDireccion = findViewById(R.id.etDireccion)


        findViewById<ImageView>(R.id.ivCalendar).setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, day ->
                etFechaNacimiento.setText("$day/${month + 1}/$year")
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }


        findViewById<ImageView>(R.id.ivSearchId).setOnClickListener {
            val id = etId.text.toString().trim()
            if (id.isEmpty()) {
                Toast.makeText(this, "Ingrese una cédula", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            buscarPorCedula(id)
        }


        findViewById<ImageView>(R.id.btnSave).setOnClickListener {
            if (validarCampos()) {
                guardarRegistro()
                Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show()
            }
        }


        findViewById<ImageView>(R.id.btnDelete).setOnClickListener {
            limpiarCampos()
            Toast.makeText(this, "Datos eliminados", Toast.LENGTH_SHORT).show()
        }


        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }


        findViewById<MaterialButton>(R.id.btnRegistrar).setOnClickListener {
            if (validarCampos()) {
                guardarRegistro()
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun validarCampos(): Boolean {
        val campos = listOf(etId, etNombre, etCorreo)
        for (campo in campos) {
            if (campo.text.toString().trim().isEmpty()) {
                campo.error = "Requerido"
                return false
            }
        }
        return true
    }

    private fun guardarRegistro() {
        val editor = prefs.edit()
        val id = etId.text.toString().trim()

        editor.putString("${id}_nombre", etNombre.text.toString())
        editor.putString("${id}_primer_apellido", etPrimerApellido.text.toString())
        editor.putString("${id}_segundo_apellido", etSegundoApellido.text.toString())
        editor.putString("${id}_fecha", etFechaNacimiento.text.toString())
        editor.putString("${id}_correo", etCorreo.text.toString())
        editor.putString("${id}_telefono", etTelefono.text.toString())
        editor.putString("${id}_provincia", etProvincia.text.toString())
        editor.putString("${id}_canton", etCanton.text.toString())
        editor.putString("${id}_distrito", etDistrito.text.toString())
        editor.putString("${id}_direccion", etDireccion.text.toString())
        editor.apply()
    }

    private fun buscarPorCedula(id: String) {
        val nombre = prefs.getString("${id}_nombre", null)
        if (nombre != null) {
            etNombre.setText(nombre)
            etPrimerApellido.setText(prefs.getString("${id}_primer_apellido", ""))
            etSegundoApellido.setText(prefs.getString("${id}_segundo_apellido", ""))
            etFechaNacimiento.setText(prefs.getString("${id}_fecha", ""))
            etCorreo.setText(prefs.getString("${id}_correo", ""))
            etTelefono.setText(prefs.getString("${id}_telefono", ""))
            etProvincia.setText(prefs.getString("${id}_provincia", ""))
            etCanton.setText(prefs.getString("${id}_canton", ""))
            etDistrito.setText(prefs.getString("${id}_distrito", ""))
            etDireccion.setText(prefs.getString("${id}_direccion", ""))
            Toast.makeText(this, "Usuario encontrado", Toast.LENGTH_SHORT).show()
        } else {
            limpiarCampos()
            etId.setText(id)
            Toast.makeText(this, "No se encontró el usuario", Toast.LENGTH_SHORT).show()
        }
    }

    private fun limpiarCampos() {
        listOf(
            etNombre, etPrimerApellido, etSegundoApellido, etFechaNacimiento,
            etCorreo, etTelefono, etProvincia, etCanton, etDistrito, etDireccion
        ).forEach { it.text.clear() }
    }
}