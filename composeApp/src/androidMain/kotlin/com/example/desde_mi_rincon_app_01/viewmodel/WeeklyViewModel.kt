package com.example.desde_mi_rincon_app_01.viewmodel

import androidx.lifecycle.ViewModel
import com.example.desde_mi_rincon_app_01.data.model.WeeklySpotlight
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WeeklyViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    // Estado que guarda la info a mostrar. Null = Cargando o vacío
    private val _spotlight = MutableStateFlow<WeeklySpotlight?>(null)
    val spotlight: StateFlow<WeeklySpotlight?> = _spotlight

    init {
        fetchLatestSpotlight()
    }

    private fun fetchLatestSpotlight() {
        db.collection("weekly_spotlights")
            .orderBy("timestamp", Query.Direction.DESCENDING) // El más nuevo primero
            .limit(1) // Solo queremos uno
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val item = result.documents[0].toObject(WeeklySpotlight::class.java)
                    _spotlight.value = item
                } else {
                    // Si no hay nada en Firebase, cargamos uno de ejemplo (Fallback)
                    loadSampleData()
                }
            }
            .addOnFailureListener {
                loadSampleData() // Si falla (ej. sin internet), muestra el ejemplo
            }
    }

    private fun loadSampleData() {
        _spotlight.value = WeeklySpotlight(
            name = "Sofía M. - Voluntaria en Refugios",
            photoUrl = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&w=300&q=80",
            quote = "\"Mi rincón seguro es un pequeño balcón con tres macetas. Allí, después de ver tanto dolor, recuerdo que la vida siempre se abre paso.\"",
            message = "A mis compañeros voluntarios: No endurezcan su corazón para protegerse. Un corazón blando es valiente, pero necesita descanso."
        )
    }
}