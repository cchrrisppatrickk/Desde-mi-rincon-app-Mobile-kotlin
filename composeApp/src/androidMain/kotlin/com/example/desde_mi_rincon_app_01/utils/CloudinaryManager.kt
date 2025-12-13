package com.example.desde_mi_rincon_app_01.utils

import android.content.Context
import android.graphics.Bitmap
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.ByteArrayOutputStream
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import androidx.annotation.Keep

@Keep
object CloudinaryManager {

    private const val CLOUD_NAME = "dw1mdguhu" // <--- CAMBIA ESTO
    private const val UPLOAD_PRESET = "rincon_app_preset"  // <--- CAMBIA ESTO (ej. rincon_app_preset)

    // Inicialización (se debe llamar una vez en el MainActivity o App)
    fun init(context: Context) {
        try {
            val config = HashMap<String, String>()
            config["cloud_name"] = CLOUD_NAME
            MediaManager.init(context, config)
        } catch (e: Exception) {
            // Ya estaba inicializado, ignoramos
        }
    }

    // Función suspendida para usar dentro de Corrutinas en el ViewModel
    suspend fun uploadBitmap(bitmap: Bitmap): String = suspendCancellableCoroutine { continuation ->

        // 1. Convertir Bitmap a ByteArray (JPG)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream) // Calidad 80%
        val byteArray = stream.toByteArray()

        // 2. Subir a Cloudinary
        MediaManager.get().upload(byteArray)
            .unsigned(UPLOAD_PRESET)
            .callback(object : UploadCallback {
                override fun onStart(requestId: String?) {}
                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}

                override fun onSuccess(requestId: String?, resultData: Map<*, *>?) {
                    // ¡Éxito! Obtenemos la URL segura (https)
                    val url = resultData?.get("secure_url").toString()
                    continuation.resume(url)
                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
                    continuation.resumeWithException(Exception("Error Cloudinary: ${error?.description}"))
                }

                override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                    continuation.resumeWithException(Exception("Rescheduled"))
                }
            })
            .dispatch()
    }
}