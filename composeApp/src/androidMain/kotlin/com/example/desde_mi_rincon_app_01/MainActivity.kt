package com.example.desde_mi_rincon_app_01

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.desde_mi_rincon_app_01.ui.screens.LockScreen
import com.example.desde_mi_rincon_app_01.utils.CloudinaryManager
import com.google.firebase.firestore.FirebaseFirestore
import androidx.annotation.Keep

@Keep
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        CloudinaryManager.init(this)

        setContent {
            // Estado para controlar qué mostrar
            // null = cargando, true = app activa, false = app bloqueada
            var isAppActive by remember { mutableStateOf<Boolean?>(null) }
            var lockMessage by remember { mutableStateOf("") }

            // EFECTO DE COMPROBACIÓN (Se ejecuta al abrir)
            LaunchedEffect(Unit) {
                val db = FirebaseFirestore.getInstance()
                val docRef = db.collection("app_config").document("general")

                // Usamos addSnapshotListener para que el cambio sea EN TIEMPO REAL.
                // Si tú cambias el valor en la consola, la app se bloquea al instante en el teléfono del cliente.
                docRef.addSnapshotListener { snapshot, e ->
                    if (e != null || snapshot == null || !snapshot.exists()) {
                        // Si hay error o no hay internet, decidimos qué hacer.
                        // Estrategia segura: Dejar entrar (Fail Open) o Bloquear (Fail Closed).
                        // Para demos, suelo dejar entrar pero con funciones limitadas.
                        // Aquí asumiremos que si falla, sigue activa por defecto:
                        isAppActive = true
                        return@addSnapshotListener
                    }

                    // Leemos el valor de la base de datos
                    val active = snapshot.getBoolean("is_active") ?: true
                    val msg = snapshot.getString("message") ?: "Versión de prueba finalizada."

                    isAppActive = active
                    lockMessage = msg
                }
            }

            // DECISIÓN DE UI
            if (isAppActive == null) {
                // 1. Cargando (Puedes poner tu SplashScreen aquí o un fondo blanco)
                Box(Modifier.fillMaxSize().background(Color(0xFF0D9488)))
            } else if (isAppActive == true) {
                // 2. App Normal (Tu código de siempre)
                App()
            } else {
                // 3. PANTALLA DE BLOQUEO (El cliente no pagó)
                LockScreen(message = lockMessage)
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}