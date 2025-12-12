package com.example.desde_mi_rincon_app_01.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.desde_mi_rincon_app_01.data.ForumPagingSource
import com.example.desde_mi_rincon_app_01.data.model.ForumPost
import com.example.desde_mi_rincon_app_01.utils.randomNames
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

    // NUEVO: Flujo de Paginación
    val postsPagingFlow = Pager(
        // Configuración: pageSize = cuántos posts trae por carga (ej. 10)
        config = PagingConfig(pageSize = 10, enablePlaceholders = false)
    ) {
        // Definimos la Query aquí (Ordenados por fecha)
        val query = db.collection("posts")
            .orderBy("timestamp", Query.Direction.DESCENDING)

        ForumPagingSource(query)
    }.flow.cachedIn(viewModelScope) // cachedIn mantiene los datos vivos al rotar pantalla

    val status: StateFlow<String?> = _status

    // Lista de nombres creativos para el modo aleatorio


    // 1. NUEVO: Mapa para guardar cambios locales temporalmente (ID -> Post Actualizado)
    private val _localChanges = MutableStateFlow<Map<String, ForumPost>>(emptyMap())
    val localChanges: StateFlow<Map<String, ForumPost>> = _localChanges

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
        if (post.id.isEmpty()) return

        // A. CALCULAR EL NUEVO ESTADO LOCALMENTE (Optimista)
        val currentLikes = post.likedBy.toMutableList()
        if (currentLikes.contains(userId)) {
            currentLikes.remove(userId) // Quitar like
        } else {
            currentLikes.add(userId)    // Poner like
        }

        // Creamos el objeto post actualizado
        val updatedPost = post.copy(likedBy = currentLikes)

        // B. ACTUALIZAR LA UI INMEDIATAMENTE (Antes de ir a internet)
        // Agregamos este post al mapa de cambios locales
        _localChanges.value = _localChanges.value + (post.id to updatedPost)

        // C. ACTUALIZAR FIREBASE EN SEGUNDO PLANO
        val postRef = db.collection("posts").document(post.id)
        postRef.update("likedBy", currentLikes)
            .addOnFailureListener {
                // Si falla, revertimos el cambio local (Rollback)
                // Quitamos el post del mapa para que vuelva a mostrar el original
                val currentMap = _localChanges.value.toMutableMap()
                currentMap.remove(post.id)
                _localChanges.value = currentMap
            }
    }

    // Función para resetear la bandera cuando cerramos el diálogo
    fun dismissSuccessDialog() {
        _showSuccessDialog.value = false
    }


    fun clearStatus() {
        _status.value = null
    }
}