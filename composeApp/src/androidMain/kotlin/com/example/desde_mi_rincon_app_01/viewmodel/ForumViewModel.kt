package com.example.desde_mi_rincon_app_01.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desde_mi_rincon_app_01.data.model.ForumPost
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ForumViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val _status = MutableStateFlow<String?>(null) // Para mostrar mensajes de "Enviado" o error

    // NUEVO: Estado para la lista de posts
    private val _posts = MutableStateFlow<List<ForumPost>>(emptyList())
    val posts: StateFlow<List<ForumPost>> = _posts

    val status: StateFlow<String?> = _status

    // Lista de nombres creativos para el modo aleatorio
    private val randomNames = listOf(
        "Viajero Silencioso", "Alma Valiente", "Estrella Fugaz",
        "Caminante", "Luz de Luna", "Brote Nuevo", "Corazón Valiente"
    )

    fun sendPost(emotion: String, message: String, inputName: String) {
        if (message.isBlank()) {
            _status.value = "El mensaje no puede estar vacío."
            return
        }

        // Si el nombre está vacío, elegimos uno al azar
        val finalAuthor = if (inputName.isBlank()) randomNames.random() else inputName

        val newPost = ForumPost(
            emotion = emotion,
            message = message,
            author = finalAuthor
        )

        viewModelScope.launch {
            _status.value = "Enviando..."
            db.collection("posts")
                .add(newPost)
                .addOnSuccessListener {
                    _status.value = "¡Mensaje compartido con éxito!"
                }
                .addOnFailureListener {
                    _status.value = "Error al enviar: ${it.localizedMessage}"
                }
        }
    }

    // Función para escuchar mensajes en tiempo real
    fun listenToAllPosts() {
        db.collection("posts")
            // .whereEqualTo("emotion", emotion) <--- ESTA LÍNEA SE ELIMINA
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val postsList = snapshot.toObjects(ForumPost::class.java)
                    _posts.value = postsList
                }
            }
    }

    fun clearStatus() {
        _status.value = null
    }
}