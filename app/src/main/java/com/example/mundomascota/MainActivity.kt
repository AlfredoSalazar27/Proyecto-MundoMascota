package com.example.mundomascota

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.Executors
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    private lateinit var previewView: PreviewView
    private var imageCapture: ImageCapture? = null
    private val cameraExecutor = Executors.newSingleThreadExecutor()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) startCamera() else finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        previewView = findViewById(R.id.previewView)

        // PERMISOS
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        // BOTÓN TOMAR FOTO
        findViewById<ImageView>(R.id.ivTomarFoto).setOnClickListener {
            takePhoto()
        }


    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                imageCapture = ImageCapture.Builder()
                    .setTargetRotation(previewView.display.rotation)
                    .build()

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, CameraSelector.DEFAULT_FRONT_CAMERA, preview, imageCapture)
            } catch (e: Exception) {
                Toast.makeText(this, "Error cámara", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        imageCapture.takePicture(cameraExecutor, object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                val buffer = image.planes[0].buffer
                val bytes = ByteArray(buffer.remaining())
                buffer.get(bytes)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                image.close()

                runOnUiThread {
                    verificarFaceID(bitmap)
                }
            }

            override fun onError(exception: ImageCaptureException) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Error al tomar foto", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun verificarFaceID(bitmapNuevo: Bitmap) {
        val prefs = getSharedPreferences("usuario_face", MODE_PRIVATE)
        val fotoPath = prefs.getString("foto_path", null)

        // SI NO HAY FOTO GUARDADA → GUARDAR TEMPORAL Y IR A REGISTRO
        if (fotoPath.isNullOrEmpty() || !File(fotoPath).exists()) {
            // GUARDAR FOTO TEMPORAL EN DISCO (NO EN INTENT)
            val tempPath = guardarFotoTemporal(bitmapNuevo)
            if (tempPath.isNotEmpty()) {
                val intent = Intent(this, RegistroActivity::class.java)
                intent.putExtra("foto_temporal_path", tempPath)  // ← Solo la ruta (String)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Error al guardar foto temporal", Toast.LENGTH_SHORT).show()
            }
            return
        }

        // SI YA HAY FOTO → COMPARAR (igual que antes)
        val fotoGuardada = BitmapFactory.decodeFile(fotoPath) ?: run {
            Toast.makeText(this, "Foto guardada dañada", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, RegistroActivity::class.java))
            finish()
            return
        }

        detectarRostro(bitmapNuevo) { caraNueva ->
            detectarRostro(fotoGuardada) { caraGuardada ->
                runOnUiThread {
                    if (caraNueva != null && caraGuardada != null) {
                        val similitud = calcularSimilitud(caraNueva, caraGuardada)
                        if (similitud > 0.70f) {
                            Toast.makeText(this, "¡Bienvenido!", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this, MenuActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Rostro no reconocido", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this, RegistroActivity::class.java))
                            finish()
                        }
                    } else {
                        Toast.makeText(this, "No se detectó rostro", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, RegistroActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }

    // NUEVA FUNCIÓN: GUARDAR FOTO TEMPORAL
    private fun guardarFotoTemporal(bitmap: Bitmap): String {
        val carpeta = File(filesDir, "temp")
        if (!carpeta.exists()) carpeta.mkdirs()
        val archivo = File(carpeta, "temp_face.jpg")

        return try {
            FileOutputStream(archivo).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)
            }
            archivo.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    private fun detectarRostro(bitmap: Bitmap, callback: (com.google.mlkit.vision.face.Face?) -> Unit) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val detector = FaceDetection.getClient(
            FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .build()
        )
        detector.process(image)
            .addOnSuccessListener { callback(it.firstOrNull()) }
            .addOnFailureListener { callback(null) }
    }

    private fun calcularSimilitud(c1: com.google.mlkit.vision.face.Face, c2: com.google.mlkit.vision.face.Face): Float {
        val l1 = c1.allLandmarks
        val l2 = c2.allLandmarks
        if (l1.isEmpty() || l2.isEmpty()) return 0f

        var distancia = 0.0
        for (i in l1.indices) {
            val p1 = l1[i].position
            val p2 = l2[i].position
            distancia += sqrt(((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y)).toDouble())
        }
        val media = distancia / l1.size
        return (1.0f / (1.0f + (media / 100f).toFloat())).coerceIn(0f, 1f)
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}