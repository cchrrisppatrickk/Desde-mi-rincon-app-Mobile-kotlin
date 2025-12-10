package com.example.desde_mi_rincon_app_01.ui.screens.capsules

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.desde_mi_rincon_app_01.data.model.VideoCapsule
import com.example.desde_mi_rincon_app_01.viewmodel.CapsulesViewModel

@Composable
fun CapsulesScreen(viewModel: CapsulesViewModel = viewModel()) {
    val capsules by viewModel.capsules.collectAsState()
    val uriHandler = LocalUriHandler.current

    // Estado para mostrar u ocultar el formulario
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // --- CABECERA CON BOTÓN ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(32.dp)
                            .background(Color(0xFF0D9488), RoundedCornerShape(2.dp))
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Cápsulas",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                }
                Text(
                    text = "Recursos para el alma.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            // BOTÓN DE SUBIDA (NUEVO)
            Button(
                onClick = { showDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D9488)),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Subir", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- GRID DE VIDEOS ---
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(capsules) { capsule ->
                VideoCard(capsule = capsule) {
                    uriHandler.openUri(capsule.videoUrl)
                }
            }
        }
    }

    // --- EL FORMULARIO FLOTANTE (DIALOG) ---
    if (showDialog) {
        AddCapsuleDialog(
            onDismiss = { showDialog = false },
            onConfirm = { title, desc, cat, url ->
                viewModel.addCapsule(title, desc, cat, url)
                showDialog = false
            }
        )
    }
}

// COMPONENTE: FORMULARIO DE SUBIDA
@Composable
fun AddCapsuleDialog(onDismiss: () -> Unit, onConfirm: (String, String, String, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var link by remember { mutableStateOf("") }

    // Selector de categoría simple
    val categories = listOf("Reflexión", "Microcuento", "Amor Propio", "Paz")
    var selectedCategory by remember { mutableStateOf(categories[0]) }
    var expandedCategory by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Nueva Cápsula",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D9488)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campos de texto
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción corta") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = link,
                    onValueChange = { link = it },
                    label = { Text("Link del video (YouTube)") },
                    placeholder = { Text("https://...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Selector de Categoría (Radio Buttons simplificados en filas)
                Text("Categoría:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Column {
                    categories.chunked(2).forEach { rowCats ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            rowCats.forEach { cat ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { selectedCategory = cat }
                                        .padding(4.dp)
                                ) {
                                    RadioButton(
                                        selected = (selectedCategory == cat),
                                        onClick = { selectedCategory = cat },
                                        colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF0D9488))
                                    )
                                    Text(text = cat, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botones de Acción
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar", color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if(title.isNotEmpty() && link.isNotEmpty()) {
                                onConfirm(title, description, selectedCategory, link)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D9488))
                    ) {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}
@Composable
fun VideoCard(capsule: VideoCapsule, onClick: () -> Unit) {
    // Definimos colores según la categoría (simulando tu CSS)
    val (badgeColor, textColor) = when(capsule.category) {
        "Reflexión" -> Pair(Color(0xFFCCFBF1), Color(0xFF0D9488)) // Teal
        "Microcuento" -> Pair(Color(0xFFFEF3C7), Color(0xFFD97706)) // Amber
        "Amor Propio" -> Pair(Color(0xFFFFE4E6), Color(0xFFE11D48)) // Rose
        "Paz" -> Pair(Color(0xFFE0E7FF), Color(0xFF4F46E5)) // Indigo
        else -> Pair(Color(0xFFF1F5F9), Color.Gray)
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() } // Todo el card es clickeable
    ) {
        Column {
            // 1. Miniatura (Simulada con un icono grande)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color(0xFFE2E8F0)), // Fondo gris claro
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayCircle,
                    contentDescription = "Play",
                    tint = Color(0xFF0D9488).copy(alpha = 0.8f),
                    modifier = Modifier.size(48.dp)
                )
            }

            // 2. Info del Video
            Column(modifier = Modifier.padding(12.dp)) {
                // Badge de Categoría
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(badgeColor)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = capsule.category.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        fontSize = 10.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = capsule.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = capsule.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 14.sp
                )
            }
        }
    }
}