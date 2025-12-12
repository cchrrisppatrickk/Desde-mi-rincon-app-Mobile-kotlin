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

@Composable
fun SplashScreen(onNavigateToHome: () -> Unit) {

    // 1. Configuración de Coil para GIFs
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

    // 2. Estados de Animación para el TEXTO
    // Alpha: Opacidad (de 0 invisible a 1 visible)
    val textAlpha = remember { Animatable(0f) }
    // OffsetY: Posición vertical (empieza 50 pixeles abajo y sube a 0)
    val textOffsetY = remember { Animatable(50f) }

    // 3. Orquestador de Tiempos
    LaunchedEffect(key1 = true) {

        // A. El GIF empieza a cargar inmediatamente al abrir la pantalla...

        // B. Esperamos un poquito (ej. 300ms) para que el GIF tome protagonismo
        delay(300)

        // C. Iniciamos la animación del TEXTO (Lanzamos corrutinas paralelas)
        launch {
            textAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1000) // Tarda 1 seg en aparecer
            )
        }
        launch {
            textOffsetY.animateTo(
                targetValue = 0f, // Sube a su posición original
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = FastOutSlowInEasing // Efecto de frenado suave al final
                )
            )
        }

        // D. Esperamos el tiempo total que quieres que dure el Splash (ej. 3.5 segundos total)
        delay(3200)

        // E. Navegamos
        onNavigateToHome()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D9488)), // Color Teal
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            // --- EL GIF ---
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(R.drawable.mi_logo_animado) // Tu archivo GIF
                    .build(),
                imageLoader = imageLoader,
                contentDescription = "Logo animado",
                modifier = Modifier.size(300.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // --- EL TEXTO ANIMADO ---
            Text(
                text = "Desde mi rincón",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    // APLICAMOS LA MAGIA AQUÍ:
                    .graphicsLayer {
                        alpha = textAlpha.value       // Aplica la transparencia actual
                        translationY = textOffsetY.value // Aplica la posición actual
                    }
            )
        }

        // Texto pequeño al pie (Opcional: también podrías animarlo igual si quisieras)
        Text(
            text = "Tu espacio seguro",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.7f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 64.dp)
                .graphicsLayer {
                    alpha = textAlpha.value // Que aparezca junto con el título
                }
        )
    }
}