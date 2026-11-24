package com.aplifans.smartpot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Green = Color(0xFF8DD6A5)
private val Background = Color(0xFF0D1512)
private val Panel = Color(0xFF19251F)
private val PanelSoft = Color(0xFF26362D)
private val Muted = Color(0xFFA6B7AC)
private val Blue = Color(0xFF54B9FF)

private data class PlantGuide(val name: String, val range: String, val advice: String)
private val plantGuides = listOf(
    PlantGuide("Monstera", "35–60%", "Dejá secar la capa superior antes de volver a regar."),
    PlantGuide("Potus", "30–55%", "Tolera períodos algo más secos; evitá el sustrato empapado."),
    PlantGuide("Sansevieria", "15–35%", "Necesita secados marcados entre riegos. Regá con moderación."),
    PlantGuide("Cactus y suculentas", "10–25%", "Usá sustrato muy drenante y esperá a que se seque bien."),
    PlantGuide("Helechos", "45–70%", "Prefieren humedad más constante, sin agua acumulada."),
    PlantGuide("Calathea", "45–65%", "Mantené humedad moderada y estable; evitá encharcar."),
    PlantGuide("Lirio de paz", "40–65%", "Regá cuando el sustrato empiece a perder humedad."),
    PlantGuide("Ficus", "30–55%", "Dejá secar parcialmente el sustrato entre riegos.")
)
private data class Pot(
    val id: Int,
    val name: String,
    val humidity: Int,
    val battery: Int,
    val location: String,
    val targetHumidity: Int
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = darkColorScheme(
                primary = Green, background = Background, surface = Panel,
                onPrimary = Color(0xFF102016), onBackground = Color(0xFFF1F5F1),
                onSurface = Color(0xFFF1F5F1)
            )) { KyntekApp() }
        }
    }
}

@Composable
private fun KyntekApp() {
    val pots = remember { mutableStateListOf(
        Pot(1, "Monstera", 48, 86, "Comedor", 50),
        Pot(2, "Sansevieria", 24, 93, "Patio", 25)
    ) }
    var showAdd by remember { mutableStateOf(false) }
    var showGuide by remember { mutableStateOf(false) }
    var editingPot by remember { mutableStateOf<Pot?>(null) }
    var newName by remember { mutableStateOf("") }
    var newLocation by remember { mutableStateOf("") }
    var newTarget by remember { mutableFloatStateOf(45f) }

    Surface(Modifier.fillMaxSize(), color = Background) {
        Box(Modifier.fillMaxSize()) {
            LazyColumn(
                Modifier.fillMaxSize().padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Spacer(Modifier.height(46.dp))
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        // Se conserva la proporción original del logo y se reduce su tamaño.
                        Image(
                            painterResource(R.drawable.kyntek_logo),
                            contentDescription = "Logo Kyntek",
                            modifier = Modifier.width(78.dp).height(58.dp),
                            contentScale = ContentScale.Fit
                        )
                        Column(Modifier.weight(1f).padding(start = 10.dp)) {
                            Text("KYNTEK", color = Green, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                            Text("MACETERO INTELIGENTE", color = Muted, fontSize = 10.sp, letterSpacing = 1.sp)
                            Spacer(Modifier.height(4.dp))
                            Text("Tu jardín, conectado", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                        }
                        IconButton(onClick = { showGuide = true }) {
                            Icon(Icons.Default.Book, "Guía de plantas", tint = Green, modifier = Modifier.size(25.dp))
                        }
                    }
                    Spacer(Modifier.height(22.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text("Mis macetas", fontSize = 27.sp, fontWeight = FontWeight.Bold)
                            Text("Estado general de tus plantas", color = Muted, fontSize = 13.sp)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("0", color = Green, fontSize = 23.sp, fontWeight = FontWeight.Bold)
                            Text("conectadas", color = Muted, fontSize = 11.sp)
                        }
                    }
                }
                item {
                    SummaryCard("MACETAS REGISTRADAS", pots.size.toString(), "en tu jardín", Modifier.fillMaxWidth())
                }
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Tus plantas", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                        TextButton(onClick = { showGuide = true }) {
                            Icon(Icons.Default.Book, null, modifier = Modifier.size(17.dp))
                            Text(" Guía")
                        }
                    }
                }
                items(pots, key = { it.id }) { pot ->
                    PotCard(pot = pot, onEdit = { editingPot = pot })
                }
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = PanelSoft),
                        shape = RoundedCornerShape(17.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.WaterDrop, null, tint = Blue)
                            Column(Modifier.padding(start = 10.dp)) {
                                Text("Lecturas de demostración", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                Text("Los porcentajes son ficticios hasta vincular el ESP32.", color = Muted, fontSize = 11.sp)
                            }
                        }
                    }
                    Spacer(Modifier.height(86.dp))
                }
            }
            FloatingActionButton(
                onClick = {
                    newName = ""
                    newLocation = ""
                    newTarget = 45f
                    showAdd = true
                },
                containerColor = Green, contentColor = Color(0xFF102016),
                modifier = Modifier.align(Alignment.BottomEnd).padding(22.dp)
            ) { Icon(Icons.Default.Add, "Agregar maceta") }
        }
    }

    if (showAdd) {
        AlertDialog(
            onDismissRequest = { showAdd = false },
            title = { Text("Nueva maceta") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(value = newName, onValueChange = { newName = it }, label = { Text("Nombre de la planta") }, singleLine = true)
                    OutlinedTextField(value = newLocation, onValueChange = { newLocation = it }, label = { Text("Ubicación (ej. Comedor, patio)") }, singleLine = true)
                    Text("Humedad objetivo: ${newTarget.toInt()}%", color = Green, fontWeight = FontWeight.SemiBold)
                    Slider(value = newTarget, onValueChange = { newTarget = it }, valueRange = 0f..100f)
                }
            },
            confirmButton = {
                TextButton(
                    enabled = newName.isNotBlank(),
                    onClick = {
                        val id = (pots.maxOfOrNull { it.id } ?: 0) + 1
                        val humidity = listOf(42, 56, 31, 68, 47)[(id - 1) % 5]
                        val battery = listOf(91, 84, 76, 98, 88)[(id - 1) % 5]
                        pots.add(Pot(id, newName.trim(), humidity, battery, newLocation.trim().ifBlank { "Sin ubicación" }, newTarget.toInt()))
                        showAdd = false
                    }
                ) { Text("Agregar") }
            },
            dismissButton = { TextButton(onClick = { showAdd = false }) { Text("Cancelar") } }
        )
    }

    editingPot?.let { pot ->
        var editName by remember(pot.id) { mutableStateOf(pot.name) }
        var editLocation by remember(pot.id) { mutableStateOf(pot.location) }
        var editTarget by remember(pot.id) { mutableFloatStateOf(pot.targetHumidity.toFloat()) }
        AlertDialog(
            onDismissRequest = { editingPot = null },
            title = { Text("Editar maceta") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(value = editName, onValueChange = { editName = it }, label = { Text("Nombre de la planta") }, singleLine = true)
                    OutlinedTextField(value = editLocation, onValueChange = { editLocation = it }, label = { Text("Ubicación") }, singleLine = true)
                    Text("Humedad objetivo: ${editTarget.toInt()}%", color = Green, fontWeight = FontWeight.SemiBold)
                    Slider(value = editTarget, onValueChange = { editTarget = it }, valueRange = 0f..100f)
                    Text("Este valor define el objetivo de humedad para esta planta. No modifica la lectura simulada del sensor.", color = Muted, fontSize = 11.sp)
                }
            },
            confirmButton = {
                TextButton(
                    enabled = editName.isNotBlank(),
                    onClick = {
                        val index = pots.indexOfFirst { it.id == pot.id }
                        if (index >= 0) pots[index] = pot.copy(
                            name = editName.trim(),
                            location = editLocation.trim().ifBlank { "Sin ubicación" },
                            targetHumidity = editTarget.toInt()
                        )
                        editingPot = null
                    }
                ) { Text("Guardar") }
            },
            dismissButton = { TextButton(onClick = { editingPot = null }) { Text("Cancelar") } }
        )
    }

    if (showGuide) {
        AlertDialog(
            onDismissRequest = { showGuide = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Book, null, tint = Green)
                    Text("  Guía de humedad")
                }
            },
            text = {
                LazyColumn(Modifier.height(430.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
                    item {
                        Text("Referencias orientativas del indicador. No son porcentajes universales de agua en el suelo.", color = Muted, fontSize = 12.sp)
                    }
                    items(plantGuides) { plant ->
                        Column(Modifier.fillMaxWidth().background(PanelSoft, RoundedCornerShape(12.dp)).padding(11.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(plant.name, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                                Text(plant.range, color = Green, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                            Spacer(Modifier.height(4.dp))
                            Text(plant.advice, color = Muted, fontSize = 11.sp)
                        }
                    }
                    item {
                        Text("Importante: el sensor capacitivo debe calibrarse con el sustrato de cada maceta. La mezcla, compactación y ubicación de la sonda pueden cambiar la lectura.", color = Muted, fontSize = 11.sp)
                    }
                }
            },
            confirmButton = { TextButton(onClick = { showGuide = false }) { Text("Cerrar") } }
        )
    }
}

@Composable
private fun SummaryCard(title: String, value: String, detail: String, modifier: Modifier = Modifier) {
    Card(modifier, colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(18.dp)) {
        Column(Modifier.padding(16.dp)) {
            Text(title, color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Spacer(Modifier.height(7.dp))
            Text(value, fontSize = 25.sp, fontWeight = FontWeight.Bold)
            Text(detail, color = Muted, fontSize = 11.sp)
        }
    }
}

@Composable
private fun PotCard(pot: Pot, onEdit: () -> Unit) {
    val humidityColor = when {
        pot.humidity < pot.targetHumidity -> Color(0xFFFFC36A)
        pot.humidity > 80 -> Blue
        else -> Green
    }
    Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 13.dp, vertical = 15.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(Modifier.size(42.dp).background(PanelSoft, CircleShape), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Eco, null, tint = Green)
                }
                IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar maceta", tint = Green, modifier = Modifier.size(18.dp))
                }
            }
            Column(Modifier.weight(1f).padding(start = 9.dp), verticalArrangement = Arrangement.Center) {
                Text(pot.name, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
                Text(pot.location, color = Muted, fontSize = 11.sp, maxLines = 1)
                Text("DEMO", color = Blue, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(5.dp))
            Column(Modifier.width(104.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.WaterDrop, null, tint = humidityColor, modifier = Modifier.size(15.dp))
                    Text(" ${pot.humidity}%", color = humidityColor, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.weight(1f))
                    Text("H", color = Muted, fontSize = 9.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.BatteryFull, null, tint = Green, modifier = Modifier.size(15.dp))
                    Text(" ${pot.battery}%", color = Green, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.weight(1f))
                    Text("B", color = Muted, fontSize = 9.sp)
                }
            }
        }
    }
}
