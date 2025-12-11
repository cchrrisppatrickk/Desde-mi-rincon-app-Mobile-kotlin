package com.example.desde_mi_rincon_app_01.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desde_mi_rincon_app_01.data.model.ForumPost
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class ForumViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    // Mantenemos _status para ERRORES (texto rojo), pero usamos este nuevo booleano para el ÉXITO
    private val _showSuccessDialog = MutableStateFlow(false)
    val showSuccessDialog: StateFlow<Boolean> = _showSuccessDialog

    private val _status = MutableStateFlow<String?>(null)

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

        val finalAuthor = if (inputName.isBlank()) randomNames.random() else inputName

        val newPost = ForumPost(
            emotion = emotion,
            message = message,
            author = finalAuthor
        )

        viewModelScope.launch {
            // Limpiamos estados previos
            _status.value = null
            val newDocRef = db.collection("posts").document() // Genera ID antes
            val postWithId = newPost.copy(id = newDocRef.id) // Asigna el ID al objeto


            newDocRef.set(postWithId) // Guarda usando set() en lugar de add()
                .addOnSuccessListener { _showSuccessDialog.value = true }
                .addOnFailureListener { _status.value = "Error..." }
        }
    }

    // 1. Obtener un ID único para este usuario (guardado en el teléfono)
    // Esto simula una sesión de usuario sin necesidad de Login complejo.
    fun getUserId(context: Context): String {
        val sharedPrefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        var uuid = sharedPrefs.getString("user_uuid", null)
        if (uuid == null) {
            uuid = UUID.randomUUID().toString()
            sharedPrefs.edit().putString("user_uuid", uuid).apply()
        }
        return uuid
    }

    // 2. Función para dar/quitar Like (Toggle)
    fun toggleLike(post: ForumPost, userId: String) {
        // Referencia al documento específico
        // NOTA: Asegúrate de que al crear el post guardes el ID del documento en el campo 'id'
        // Si 'post.id' está vacío, no funcionará. Ver corrección abajo*.
        if (post.id.isEmpty()) return

        val postRef = db.collection("posts").document(post.id)

        val currentLikes = post.likedBy.toMutableList()

        if (currentLikes.contains(userId)) {
            // Si ya dio like, lo quitamos (Dislike)
            currentLikes.remove(userId)
        } else {
            // Si no, lo agregamos
            currentLikes.add(userId)
        }

        // Actualizamos solo el campo 'likedBy' en Firestore
        postRef.update("likedBy", currentLikes)
            .addOnFailureListener { e ->
                _status.value = "Error al dar like: ${e.localizedMessage}"
            }
    }

    // Función para resetear la bandera cuando cerramos el diálogo
    fun dismissSuccessDialog() {
        _showSuccessDialog.value = false
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