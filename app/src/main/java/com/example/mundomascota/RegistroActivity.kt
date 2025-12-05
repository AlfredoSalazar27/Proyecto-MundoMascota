package com.example.mundomascota

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.*
import java.util.concurrent.Executors

// IMPORTS DE RETROFIT
import com.example.mundomascota.network.RetrofitClient
import com.example.mundomascota.network.UsuarioRequest

class RegistroActivity : AppCompatActivity() {

    // === VISTAS ===
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
    private lateinit var ivFotoRostro: ImageView

    private val facePrefs by lazy { getSharedPreferences("usuario_face", MODE_PRIVATE) }
    private var fotoBitmap: Bitmap? = null
    private lateinit var previewView: PreviewView
    private var imageCapture: ImageCapture? = null
    private val cameraExecutor = Executors.newSingleThreadExecutor()

    // === GALERÍA ===
    private val galeriaLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri ?: return@registerForActivityResult
        try {
            val inputStream = contentResolver.openInputStream(uri)
            fotoBitmap = BitmapFactory.decodeStream(inputStream)
            ivFotoRostro.setImageBitmap(fotoBitmap)
            Toast.makeText(this, "Foto seleccionada de galería", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error al cargar foto", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        inicializarVistas()
        setupCamera()

        // === FLECHA IZQUIERDA - VOLVER AL MAINACTIVITY ===
        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // === BOTÓN REGISTRAR (GUARDA EN NUBE + PASA AL MENÚ SIEMPRE) ===
        findViewById<MaterialButton>(R.id.btnRegistrar).setOnClickListener {
            if (validarCampos() && fotoBitmap != null) {
                registrarUsuarioYIrAlMenu()
            } else {
                Toast.makeText(this, "Completa todos los datos y toma una foto", Toast.LENGTH_LONG).show()
            }
        }

        // === LUPA - BUSCAR POR CÉDULA ===
        findViewById<ImageView>(R.id.ivSearchId).setOnClickListener {
            val cedula = etId.text.toString().trim()
            if (cedula.isEmpty()) {
                Toast.makeText(this, "Escribe una cédula", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            buscarUsuarioPorCedula(cedula)
        }

        // === GUARDAR LOCAL CON DISQUETE ===
        findViewById<ImageView>(R.id.btnSave).setOnClickListener {
            if (validarCamposBasicos()) {
                guardarDatosLocalmente()
                Toast.makeText(this, "Datos guardados localmente", Toast.LENGTH_SHORT).show()
            }
        }

        // === ELIMINAR CON BASURERO ===
        findViewById<ImageView>(R.id.btnDelete).setOnClickListener {
            limpiarTodo()
            Toast.makeText(this, "Datos eliminados", Toast.LENGTH_SHORT).show()
        }

        // === SALIR CON LA X ===
        findViewById<ImageView>(R.id.btnClose).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // === CALENDARIO ===
        findViewById<ImageView>(R.id.ivCalendar).setOnClickListener {
            mostrarCalendario()
        }

        // === GALERÍA ===
        findViewById<ImageView>(R.id.ivGaleria).setOnClickListener {
            galeriaLauncher.launch("image/*")
        }

        // === CÁMARA ===
        findViewById<ImageView>(R.id.ivTomarFoto).setOnClickListener {
            takePhoto()
        }
    }

    // === REGISTRAR Y SIEMPRE IR AL MENÚ ===
    private fun registrarUsuarioYIrAlMenu() {
        guardarFaceIDLocal()
        guardarDatosLocalmente()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.apiService.registrarUsuario(
                    UsuarioRequest(
                        nombre = etNombre.text.toString().trim(),
                        correo = etCorreo.text.toString().trim(),
                        telefono = etTelefono.text.toString().trim()
                    )
                )

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@RegistroActivity, "¡Registrado en la nube!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@RegistroActivity, "Guardado local (sin internet)", Toast.LENGTH_LONG).show()
                    }
                    startActivity(Intent(this@RegistroActivity, MenuActivity::class.java))
                    finish()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RegistroActivity, "Guardado local (sin internet)", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@RegistroActivity, MenuActivity::class.java))
                    finish()
                }
            }
        }
    }

    // === BUSCAR USUARIO POR CÉDULA ===
    private fun buscarUsuarioPorCedula(cedula: String) {
        val prefs = getSharedPreferences("RegistroUsuario", MODE_PRIVATE)
        val nombre = prefs.getString("${cedula}_nombre", null)

        if (nombre != null) {
            etNombre.setText(nombre)
            etPrimerApellido.setText(prefs.getString("${cedula}_primer_apellido", "") ?: "")
            etSegundoApellido.setText(prefs.getString("${cedula}_segundo_apellido", "") ?: "")
            etFechaNacimiento.setText(prefs.getString("${cedula}_fecha", "") ?: "")
            etCorreo.setText(prefs.getString("${cedula}_correo", "") ?: "")
            etTelefono.setText(prefs.getString("${cedula}_telefono", "") ?: "")
            etProvincia.setText(prefs.getString("${cedula}_provincia", "") ?: "")
            etCanton.setText(prefs.getString("${cedula}_canton", "") ?: "")
            etDistrito.setText(prefs.getString("${cedula}_distrito", "") ?: "")
            etDireccion.setText(prefs.getString("${cedula}_direccion", "") ?: "")

            val path = facePrefs.getString("foto_path", null)
            if (path != null && File(path).exists()) {
                fotoBitmap = BitmapFactory.decodeFile(path)
                ivFotoRostro.setImageBitmap(fotoBitmap)
            }

            Toast.makeText(this, "Usuario encontrado", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "No existe ese usuario", Toast.LENGTH_SHORT).show()
        }
    }

    private fun guardarDatosLocalmente() {
        val id = etId.text.toString().trim()
        val prefs = getSharedPreferences("RegistroUsuario", MODE_PRIVATE).edit()
        prefs.apply {
            putString("${id}_nombre", etNombre.text.toString())
            putString("${id}_primer_apellido", etPrimerApellido.text.toString())
            putString("${id}_segundo_apellido", etSegundoApellido.text.toString())
            putString("${id}_fecha", etFechaNacimiento.text.toString())
            putString("${id}_correo", etCorreo.text.toString())
            putString("${id}_telefono", etTelefono.text.toString())
            putString("${id}_provincia", etProvincia.text.toString())
            putString("${id}_canton", etCanton.text.toString())
            putString("${id}_distrito", etDistrito.text.toString())
            putString("${id}_direccion", etDireccion.text.toString())
            apply()
        }
    }

    private fun guardarFaceIDLocal() {
        fotoBitmap?.let { bitmap ->
            val path = guardarFotoFaceID(bitmap, etId.text.toString())
            facePrefs.edit().apply {
                putString("foto_path", path)
                putString("nombre_usuario", etNombre.text.toString())
                apply()
            }
        }
    }

    private fun guardarFotoFaceID(bitmap: Bitmap, id: String): String {
        val carpeta = File(filesDir, "fotos_face_id")
        if (!carpeta.exists()) carpeta.mkdirs()
        val file = File(carpeta, "face_$id.jpg")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }
        return file.absolutePath
    }

    private fun mostrarCalendario() {
        val c = Calendar.getInstance()
        DatePickerDialog(this, { _, year, month, day ->
            etFechaNacimiento.setText("$day/${month + 1}/$year")
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun limpiarTodo() {
        etId.text.clear()
        etNombre.text.clear()
        etPrimerApellido.text.clear()
        etSegundoApellido.text.clear()
        etFechaNacimiento.text.clear()
        etCorreo.text.clear()
        etTelefono.text.clear()
        etProvincia.text.clear()
        etCanton.text.clear()
        etDistrito.text.clear()
        etDireccion.text.clear()
        ivFotoRostro.setImageResource(R.drawable.ic_camera)
        fotoBitmap = null
    }

    private fun validarCampos(): Boolean {
        return etId.text.isNotEmpty() &&
                etNombre.text.isNotEmpty() &&
                etCorreo.text.isNotEmpty() &&
                fotoBitmap != null
    }

    private fun validarCamposBasicos(): Boolean {
        return etId.text.isNotEmpty() && etNombre.text.isNotEmpty()
    }

    // === CÁMARA ===
    private fun inicializarVistas() {
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
        ivFotoRostro = findViewById(R.id.ivTomarFoto)
        previewView = findViewById(R.id.previewView)
    }

    private fun setupCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }
            imageCapture = ImageCapture.Builder().build()
            cameraProvider.bindToLifecycle(this, CameraSelector.DEFAULT_FRONT_CAMERA, preview, imageCapture)
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        imageCapture?.takePicture(cameraExecutor, object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                val buffer = image.planes[0].buffer
                val bytes = ByteArray(buffer.remaining())
                buffer.get(bytes)
                fotoBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                image.close()
                runOnUiThread {
                    ivFotoRostro.setImageBitmap(fotoBitmap)
                    Toast.makeText(this@RegistroActivity, "¡Foto tomada!", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}