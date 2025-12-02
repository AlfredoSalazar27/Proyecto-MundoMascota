// RegistroActivity.kt - CON GALERÍA + CÁMARA REAL
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
import java.io.File
import java.io.FileOutputStream
import java.util.*
import java.util.concurrent.Executors

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

    // === PREFERENCIAS ===
    private val prefs by lazy { getSharedPreferences("RegistroUsuario", MODE_PRIVATE) }
    private val facePrefs by lazy { getSharedPreferences("usuario_face", MODE_PRIVATE) }

    // === FOTO DEL ROSTRO ===
    private var fotoBitmap: Bitmap? = null

    // === CÁMARA ===
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

        // RECIBIR FOTO TEMPORAL
        val tempPath = intent.getStringExtra("foto_temporal_path")
        if (tempPath != null && File(tempPath).exists()) {
            fotoBitmap = BitmapFactory.decodeFile(tempPath)
            ivFotoRostro.setImageBitmap(fotoBitmap)
            Toast.makeText(this, "Foto cargada", Toast.LENGTH_LONG).show()
            File(tempPath).delete()
        }

        // CALENDARIO
        findViewById<ImageView>(R.id.ivCalendar).setOnClickListener { mostrarDatePicker() }

        // BUSCAR POR CÉDULA
        findViewById<ImageView>(R.id.ivSearchId).setOnClickListener {
            val id = etId.text.toString().trim()
            if (id.isEmpty()) {
                Toast.makeText(this, "Escribe una cédula", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            buscarPorCedula(id)
        }

        // TOMAR FOTO
        findViewById<ImageView>(R.id.ivTomarFoto).setOnClickListener {
            takePhoto()
        }

        // ABRIR GALERÍA
        findViewById<ImageView>(R.id.ivGaleria).setOnClickListener {
            galeriaLauncher.launch("image/*")
        }

        // REGISTRAR
        findViewById<MaterialButton>(R.id.btnRegistrar).setOnClickListener {
            if (validarCampos() && fotoBitmap != null) {
                guardarRegistroConFaceID()
                Toast.makeText(this, "¡Registro exitoso con Face ID!", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, MenuActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Completa todos los datos y toma una foto", Toast.LENGTH_LONG).show()
            }
        }

        // GUARDAR SIN FOTO
        findViewById<ImageView>(R.id.btnSave).setOnClickListener {
            if (validarCamposBasicos()) {
                guardarSoloDatos()
                Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show()
            }
        }

        // LIMPIAR
        findViewById<ImageView>(R.id.btnDelete).setOnClickListener {
            limpiarTodo()
            Toast.makeText(this, "Todo limpiado", Toast.LENGTH_SHORT).show()
        }

        // VOLVER
        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

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
            try {
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                imageCapture = ImageCapture.Builder().build()
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, CameraSelector.DEFAULT_FRONT_CAMERA, preview, imageCapture)
            } catch (e: Exception) {
                Toast.makeText(this, "Error al iniciar cámara", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        imageCapture ?: return
        imageCapture!!.takePicture(cameraExecutor, object : ImageCapture.OnImageCapturedCallback() {
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

            override fun onError(e: ImageCaptureException) {
                runOnUiThread {
                    Toast.makeText(this@RegistroActivity, "Error al tomar foto", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun mostrarDatePicker() {
        val c = Calendar.getInstance()
        DatePickerDialog(this, { _, y, m, d -> etFechaNacimiento.setText("$d/${m+1}/$y") },
            c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun validarCampos(): Boolean {
        return etId.text.isNotEmpty() && etNombre.text.isNotEmpty() && etCorreo.text.isNotEmpty() && fotoBitmap != null
    }

    private fun validarCamposBasicos(): Boolean {
        return etId.text.isNotEmpty() && etNombre.text.isNotEmpty()
    }

    private fun guardarRegistroConFaceID() {
        val id = etId.text.toString().trim()
        guardarDatosUsuario(id)

        fotoBitmap?.let { bitmap ->
            val path = guardarFotoFaceID(bitmap, id)
            facePrefs.edit().apply {
                putString("foto_path", path)
                putString("nombre_usuario", etNombre.text.toString().trim())
                apply()
            }
        }
    }

    private fun guardarSoloDatos() {
        val id = etId.text.toString().trim()
        guardarDatosUsuario(id)
    }

    private fun guardarDatosUsuario(id: String) {
        prefs.edit().apply {
            putString("${id}_nombre", etNombre.text.toString().trim())
            putString("${id}_primer_apellido", etPrimerApellido.text.toString().trim())
            putString("${id}_segundo_apellido", etSegundoApellido.text.toString().trim())
            putString("${id}_fecha", etFechaNacimiento.text.toString().trim())
            putString("${id}_correo", etCorreo.text.toString().trim())
            putString("${id}_telefono", etTelefono.text.toString().trim())
            putString("${id}_provincia", etProvincia.text.toString().trim())
            putString("${id}_canton", etCanton.text.toString().trim())
            putString("${id}_distrito", etDistrito.text.toString().trim())
            putString("${id}_direccion", etDireccion.text.toString().trim())
            apply()
        }
    }

    private fun guardarFotoFaceID(bitmap: Bitmap, id: String): String {
        val carpeta = File(filesDir, "fotos_face_id")
        if (!carpeta.exists()) carpeta.mkdirs()
        val file = File(carpeta, "face_$id.jpg")
        try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file.absolutePath
    }

    private fun buscarPorCedula(id: String) {
        val nombre = prefs.getString("${id}_nombre", null)
        if (nombre != null) {
            etNombre.setText(nombre)
            etPrimerApellido.setText(prefs.getString("${id}_primer_apellido", "") ?: "")
            etSegundoApellido.setText(prefs.getString("${id}_segundo_apellido", "") ?: "")
            etFechaNacimiento.setText(prefs.getString("${id}_fecha", "") ?: "")
            etCorreo.setText(prefs.getString("${id}_correo", "") ?: "")
            etTelefono.setText(prefs.getString("${id}_telefono", "") ?: "")
            etProvincia.setText(prefs.getString("${id}_provincia", "") ?: "")
            etCanton.setText(prefs.getString("${id}_canton", "") ?: "")
            etDistrito.setText(prefs.getString("${id}_distrito", "") ?: "")
            etDireccion.setText(prefs.getString("${id}_direccion", "") ?: "")
            Toast.makeText(this, "Usuario encontrado", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No existe ese usuario", Toast.LENGTH_SHORT).show()
        }
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

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}