package com.example.desde_mi_rincon_app_01.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desde_mi_rincon_app_01.data.model.VideoCapsule
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class CapsulesViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val _capsules = MutableStateFlow<List<VideoCapsule>>(emptyList())
    val capsules: StateFlow<List<VideoCapsule>> = _capsules

    // Estado de carga para mostrar una barra de progreso mientras sube
    private val _isUploading = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> = _isUploading

    init {
        fetchCapsules()
    }

    private fun fetchCapsules() {
        db.collection("capsules")
            .get()
            .addOnSuccessListener { result ->
                val list = result.toObjects(VideoCapsule::class.java)
                if (list.isEmpty()) {
                    // Si está vacío, subimos los datos de prueba del HTML automáticamente
                    uploadSampleData()
                } else {
                    _capsules.value = list
                }
            }
    }

    // Función auxiliar para cargar tus datos de ejemplo a Firestore una sola vez
    private fun uploadSampleData() {
        val samples = listOf(
            VideoCapsule(
                title = "Soltar para avanzar",
                description = "Un mensaje corto sobre cómo dejar ir las cargas que no te pertenecen.",
                category = "Reflexión",
                videoUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ", // Link de prueba
                duration = "2 min"
            ),
            VideoCapsule(
                title = "El niño y la estrella",
                description = "Una historia real de voluntariado en la selva.",
                category = "Microcuento",
                videoUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                duration = "5 min"
            ),
            VideoCapsule(
                title = "No eres infinito",
                description = "Recordatorio: descansa, te lo mereces.",
                category = "Amor Propio",
                videoUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                duration = "1 min"
            ),
            VideoCapsule(
                title = "Respiración guiada",
                description = "3 minutos de calma para momentos de crisis.",
                category = "Paz",
                videoUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                duration = "3 min"
            )
        )

        samples.forEach { capsule ->
            db.collection("capsules").add(capsule)
        }
        // Actualizamos la vista local
        _capsules.value = samples
    }

    // NUEVA FUNCIÓN: Guardar cápsula
    fun addCapsule(title: String, description: String, category: String, url: String) {
        val newCapsule = VideoCapsule(
            title = title,
            description = description,
            category = category,
            videoUrl = url,
            duration = "Video Link" // Texto por defecto
        )

        db.collection("capsules")
            .add(newCapsule)
            .addOnSuccessListener {
                // Al guardar con éxito, recargamos la lista para ver el nuevo video
                fetchCapsules()
            }
            .addOnFailureListener { e ->
                // Aquí podrías manejar el error (Log o mensaje)
            }
    }

}