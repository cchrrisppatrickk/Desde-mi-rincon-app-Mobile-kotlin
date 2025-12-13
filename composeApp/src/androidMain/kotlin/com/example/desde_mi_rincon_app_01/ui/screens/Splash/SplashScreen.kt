package com.example.desde_mi_rincon_app_01.ui.screens

import android.os.Build.VERSION.SDK_INT
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.example.desde_mi_rincon_app_01.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.annotation.Keep

@Keep
@Composable
fun SplashScreen(onNavigateToHome: () -> Unit) {

    // 1. Configuración de Coil
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    // 2. Estados de Animación de ENTRADA
    val textAlpha = remember { Animatable(0f) }
    val textOffsetY = remember { Animatable(50f) }

    // 3. NUEVO: Estado de Animación de SALIDA (Empieza visible = 1f)
    val contentExitAlpha = remember { Animatable(1f) }

    // 4. Orquestador de Tiempos
    LaunchedEffect(key1 = true) {

        // A. Espera inicial para el GIF
        delay(300)

        // B. Animación de ENTRADA del texto
        launch {
            textAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1000)
            )
        }
        launch {
            textOffsetY.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
            )
        }

        // C. Tiempo de lectura (Mantener la pantalla visible)
        // Reduje un poco este tiempo para dar espacio a la animación de salida
        delay(2500)

        // D. NUEVO: Animación de SALIDA (Desvanecer todo)
        contentExitAlpha.animateTo(
            targetValue = 0f, // Se vuelve invisible
            animationSpec = tween(durationMillis = 800) // Tarda casi un segundo en desvanecerse
        )

        // E. Navegar
        onNavigateToHome()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D9488)), // Fondo Teal
        contentAlignment = Alignment.Center
    ) {
        // Envolvemos el contenido principal en una Columna que obedece a 'contentExitAlpha'
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.graphicsLayer {
                // AQUÍ ESTÁ LA CLAVE:
                // Si contentExitAlpha baja a 0, todo esto desaparece suavemente.
                alpha = contentExitAlpha.value
            }
        ) {
            // GIF
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(R.drawable.mi_logo_animado)
                    .build(),
                imageLoader = imageLoader,
                contentDescription = "Logo animado",
                modifier = Modifier.size(300.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // TEXTO
            Text(
                text = "Desde mi rincón",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.graphicsLayer {
                    // Combinamos la opacidad de entrada (textAlpha) con la de salida general
                    // Si no estamos saliendo, usa textAlpha. Si estamos saliendo, usa contentExitAlpha.
                    alpha = textAlpha.value
                    translationY = textOffsetY.value
                }
            )
        }

        // Texto pequeño al pie (También se desvanece al salir)
        Text(
            text = "Tu espacio seguro",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.7f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 64.dp)
                .graphicsLayer {
                    // Truco matemático: Multiplicamos las opacidades.
                    // Entrada * Salida.
                    // Al inicio: 0 * 1 = 0 (Invisible)
                    // Al medio: 1 * 1 = 1 (Visible)
                    // Al final: 1 * 0 = 0 (Se desvanece)
                    alpha = textAlpha.value * contentExitAlpha.value
                }
        )
    }
}