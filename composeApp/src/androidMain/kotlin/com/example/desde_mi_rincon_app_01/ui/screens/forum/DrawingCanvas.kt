package com.example.desde_mi_rincon_app_01.ui.components

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DrawingCanvas(
    onDrawingUpdated: (Bitmap?) -> Unit
) {
    // 1. Estado para guardar la imagen (Bitmap)
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val androidCanvas = remember { mutableStateOf<Canvas?>(null) }

    // Estado para saber si el lienzo está vacío (para mostrar el placeholder)
    var isEmpty by remember { mutableStateOf(true) }

    // Configuración del Pincel
    val paint = remember {
        Paint().apply {
            color = android.graphics.Color.parseColor("#0D9488") // Teal
            isAntiAlias = true
            strokeWidth = 12f // Un poco más grueso para que se vea mejor
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
        }
    }

    var lastTouchX by remember { mutableFloatStateOf(0f) }
    var lastTouchY by remember { mutableFloatStateOf(0f) }
    var refreshTrigger by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp) // Un poco más alto para mayor comodidad
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            // Agregamos un borde sutil para delimitar la zona
            .border(BorderStroke(1.dp, Color(0xFFE2E8F0)), RoundedCornerShape(12.dp))
            .onSizeChanged { size ->
                if (size.width > 0 && size.height > 0) {
                    if (bitmap == null) {
                        val newBitmap = Bitmap.createBitmap(size.width, size.height, Bitmap.Config.ARGB_8888)
                        bitmap = newBitmap
                        androidCanvas.value = Canvas(newBitmap)
                    }
                }
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        lastTouchX = offset.x
                        lastTouchY = offset.y
                        isEmpty = false // Al tocar, ocultamos el texto de ayuda
                    },
                    onDragEnd = {
                        onDrawingUpdated(bitmap)
                    },
                    onDrag = { change, _ ->
                        val currentX = change.position.x
                        val currentY = change.position.y

                        androidCanvas.value?.drawLine(lastTouchX, lastTouchY, currentX, currentY, paint)

                        lastTouchX = currentX
                        lastTouchY = currentY
                        refreshTrigger++
                    }
                )
            }
    ) {
        // 1. TEXTO DE FONDO (Placeholder)
        // Se muestra solo si no se ha dibujado nada
        AnimatedVisibility(
            visible = isEmpty,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Brush,
                    contentDescription = null,
                    tint = Color.LightGray,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Dibuja aquí tu emoción...",
                    color = Color.LightGray,
                    fontSize = 14.sp,
                    fontStyle = FontStyle.Italic
                )
            }
        }

        // 2. EL DIBUJO REAL
        bitmap?.let { btm ->
            key(refreshTrigger) {
                Image(
                    bitmap = btm.asImageBitmap(),
                    contentDescription = "Dibujo usuario",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // 3. BARRA DE HERRAMIENTAS (FLOTANTE)
        // Usamos una fila en la parte superior derecha para los controles
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Indicador de Pincel (Visual)
            Surface(
                color = Color(0xFF0D9488).copy(alpha = 0.1f),
                shape = CircleShape,
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Brush,
                        contentDescription = "Pincel",
                        tint = Color(0xFF0D9488),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Botón Borrar (Ahora es un Icono rojo sutil)
            FilledTonalIconButton(
                onClick = {
                    androidCanvas.value?.drawColor(android.graphics.Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
                    refreshTrigger++
                    onDrawingUpdated(null)
                    isEmpty = true // Vuelve a mostrar el texto de ayuda
                },
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                    containerColor = Color(0xFFFEE2E2), // Rojo muy claro
                    contentColor = Color(0xFFEF4444)    // Rojo intenso
                ),
                modifier = Modifier.size(32.dp) // Botón pequeño y discreto
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Borrar todo",
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}