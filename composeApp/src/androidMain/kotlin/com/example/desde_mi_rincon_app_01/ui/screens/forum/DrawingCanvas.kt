package com.example.desde_mi_rincon_app_01.ui.components

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DrawingCanvas() {
    // 1. Estado para guardar la imagen (Bitmap) donde pintaremos
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    // 2. Necesitamos un Canvas de Android (clásico) para pintar sobre el Bitmap
    // Usamos 'remember' para no recrearlo en cada recomposición
    val androidCanvas = remember { mutableStateOf<Canvas?>(null) }

    // Configuración del Pincel (Paint clásico de Android es más rápido aquí)
    val paint = remember {
        Paint().apply {
            color = android.graphics.Color.parseColor("#0D9488") // Tu color Teal
            isAntiAlias = true
            strokeWidth = 10f
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
        }
    }

    // Guardamos la última posición para trazar líneas continuas
    var lastTouchX by remember { mutableFloatStateOf(0f) }
    var lastTouchY by remember { mutableFloatStateOf(0f) }

    // Trigger para forzar a Compose a actualizar la imagen cuando pintamos
    var refreshTrigger by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .onSizeChanged { size ->
                // Creamos el Bitmap solo cuando sabemos el tamaño de la pantalla
                if (size.width > 0 && size.height > 0) {
                    val newBitmap = Bitmap.createBitmap(
                        size.width,
                        size.height,
                        Bitmap.Config.ARGB_8888
                    )
                    bitmap = newBitmap
                    androidCanvas.value = Canvas(newBitmap)
                }
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        lastTouchX = offset.x
                        lastTouchY = offset.y
                    },
                    onDrag = { change, _ ->
                        // Aquí ocurre la magia de optimización
                        val currentX = change.position.x
                        val currentY = change.position.y

                        // Pintamos DIRECTAMENTE en el Bitmap (no en la UI de Compose)
                        androidCanvas.value?.drawLine(
                            lastTouchX, lastTouchY,
                            currentX, currentY,
                            paint
                        )

                        // Actualizamos coordenadas
                        lastTouchX = currentX
                        lastTouchY = currentY

                        // Forzamos a Compose a redibujar la imagen actualizada
                        refreshTrigger++
                    }
                )
            }
    ) {
        // Mostramos el Bitmap como una imagen estática
        // Esto es súper rápido porque Compose solo renderiza 1 objeto (la imagen)
        bitmap?.let { btm ->
            // Usamos refreshTrigger en un bloque key o state read para asegurar recomposición
            // Aunque al pasar 'btm.asImageBitmap()' a veces es suficiente, el trigger asegura fluidez
            key(refreshTrigger) {
                Image(
                    bitmap = btm.asImageBitmap(),
                    contentDescription = "Dibujo usuario",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Botón Borrar
        Button(
            onClick = {
                // Para borrar, simplemente limpiamos el Bitmap transparente
                androidCanvas.value?.drawColor(android.graphics.Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
                refreshTrigger++ // Forzamos actualización visual
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White.copy(alpha = 0.9f),
                contentColor = Color.Red
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text("Borrar", fontSize = 12.sp)
        }
    }
}