package com.example.desde_mi_rincon_app_01

import androidx.lifecycle.ViewModel
import com.example.desde_mi_rincon_app_01.data.model.Post
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ForumViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    // Referencia a la colección "foro_posts"
    private val collection = db.collection("foro_posts")

    fun enviarMensaje(nombreUsuario: String, mensaje: String, emocion: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        // 1. Validar nombre
        val autorFinal = if (nombreUsuario.isBlank()) "Invitado" else nombreUsuario

        // 2. Crear objeto Post (Firestore genera el ID automáticamente, lo dejamos vacío al inicio)
        val nuevoPost = Post(
            autor = autorFinal,
            mensaje = mensaje,
            emocion = emocion,
            fecha = System.currentTimeMillis()
        )

        // 3. Subir a Firestore
        collection.add(nuevoPost)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Error desconocido")
            }
    }
}