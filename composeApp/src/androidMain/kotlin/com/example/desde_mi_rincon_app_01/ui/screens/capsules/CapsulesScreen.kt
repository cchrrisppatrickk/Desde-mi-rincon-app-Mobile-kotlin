package com.example.desde_mi_rincon_app_01.ui.screens.capsules

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.desde_mi_rincon_app_01.data.model.VideoCapsule
import com.example.desde_mi_rincon_app_01.viewmodel.CapsulesViewModel

// --- PALETA DE COLORES LOCAL (Minimalista) ---
private val TealPrimary = Color(0xFF0D9488)
private val TextDark = Color(0xFF1E293B)
private val TextLight = Color(0xFF64748B)

@Composable
fun CapsulesScreen(viewModel: CapsulesViewModel = viewModel()) {
    val capsules by viewModel.capsules.collectAsState()
    val uriHandler = LocalUriHandler.current

    var showDialog by remember { mutableStateOf(false) }

    // Fondo general limpio
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA)) // Gris muy, muy claro (Casi blanco)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // --- CABECERA ---
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Cápsulas",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextDark,
                        letterSpacing = (-0.5).sp
                    )
                )
                Text(
                    text = "Recursos para sanar.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextLight
                )
            }

            // Botón "Tonal" (Más moderno que el botón sólido fuerte)
            FilledTonalButton(
                onClick = { showDialog = true },
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = TealPrimary.copy(alpha = 0.1f),
                    contentColor = TealPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Subir", fontWeight = FontWeight.SemiBold)
            }
        }

        // --- GRID DE VIDEOS ---
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 80.dp) // Espacio para scroll final
        ) {
            items(capsules) { capsule ->
                MinimalVideoCard(capsule = capsule) {
                    uriHandler.openUri(capsule.videoUrl)
                }
            }
        }
    }

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

// --- NUEVA TARJETA MINIMALISTA ---
@Composable
fun MinimalVideoCard(capsule: VideoCapsule, onClick: () -> Unit) {
    // Generar un gradiente basado en la categoría para que no se vea gris plano
    val gradientBrush = getGradientByCategory(capsule.category)

    ElevatedCard(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), // Sombra suave
        modifier = Modifier.fillMaxWidth().aspectRatio(0.8f) // Relación de aspecto vertical
    ) {
        Column {
            // 1. Área Visual (Thumbnail abstracto)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Ocupa la mayor parte
                    .background(gradientBrush),
                contentAlignment = Alignment.Center
            ) {
                // Badge de Categoría FLOTANTE (Estilo moderno)
                Surface(
                    color = Color.Black.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(bottomEnd = 12.dp),
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Text(
                        text = capsule.category.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }

                // Botón Play central minimalista
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.White.copy(alpha = 0.3f), shape = RoundedCornerShape(50)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            // 2. Información
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = capsule.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = capsule.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextLight,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// --- NUEVO DIÁLOGO PROFESIONAL ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCapsuleDialog(onDismiss: () -> Unit, onConfirm: (String, String, String, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var link by remember { mutableStateOf("") }

    val categories = listOf("Reflexión", "Microcuento", "Amor Propio", "Paz")
    var selectedCategory by remember { mutableStateOf(categories[0]) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false) // Ocupa ancho personalizado
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth(0.9f).padding(16.dp),
            shadowElevation = 8.dp
        ) {
            Column(modifier = Modifier.padding(24.dp)) {

                // Header del Dialog
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Nueva Cápsula",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Rounded.Close, contentDescription = "Cerrar", tint = TextLight)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Inputs estilizados
                CustomTextField(value = title, onValueChange = { title = it }, label = "Título")
                Spacer(modifier = Modifier.height(12.dp))
                CustomTextField(value = description, onValueChange = { description = it }, label = "Breve descripción", maxLines = 3)
                Spacer(modifier = Modifier.height(12.dp))
                CustomTextField(value = link, onValueChange = { link = it }, label = "Enlace de YouTube")

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Categoría",
                    style = MaterialTheme.typography.labelLarge,
                    color = TextDark,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))

                // CHIPS SELECCIONABLES (Mucho mejor que RadioButtons)
                OptIn(ExperimentalLayoutApi::class)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    categories.forEach { category ->
                        val isSelected = selectedCategory == category
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCategory = category },
                            label = { Text(category) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = TealPrimary.copy(alpha = 0.15f),
                                selectedLabelColor = TealPrimary,
                                labelColor = TextLight
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) TealPrimary else Color.LightGray
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Botón de acción Full Width
                Button(
                    onClick = {
                        if (title.isNotEmpty() && link.isNotEmpty()) {
                            onConfirm(title, description, selectedCategory, link)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
                ) {
                    Text("Publicar Cápsula", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// --- COMPONENTES AUXILIARES ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    maxLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        maxLines = maxLines,
        shape = RoundedCornerShape(12.dp),
        // --- AQUÍ ESTÁ EL CAMBIO ---
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = TealPrimary,
            unfocusedBorderColor = Color(0xFFE2E8F0),
            // En la versión nueva, 'containerColor' se divide en focused y unfocused.
            // Los ponemos iguales para mantener tu diseño:
            focusedContainerColor = Color(0xFFF8FAFC),
            unfocusedContainerColor = Color(0xFFF8FAFC),
            cursorColor = TealPrimary,
            focusedLabelColor = TealPrimary
        )
    )
}

// Función para obtener colores bonitos según categoría
fun getGradientByCategory(category: String): Brush {
    return when (category) {
        "Reflexión" -> Brush.linearGradient(listOf(Color(0xFF2DD4BF), Color(0xFF0D9488))) // Teals
        "Microcuento" -> Brush.linearGradient(listOf(Color(0xFFFCD34D), Color(0xFFF59E0B))) // Ambers
        "Amor Propio" -> Brush.linearGradient(listOf(Color(0xFFFDA4AF), Color(0xFFE11D48))) // Roses
        "Paz" -> Brush.linearGradient(listOf(Color(0xFFA5B4FC), Color(0xFF6366F1))) // Indigo
        else -> Brush.linearGradient(listOf(Color(0xFFCBD5E1), Color(0xFF94A3B8))) // Gray
    }
}