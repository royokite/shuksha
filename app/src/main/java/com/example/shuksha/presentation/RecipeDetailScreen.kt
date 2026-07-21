package com.example.shuksha.presentation

import android.content.Intent
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.shuksha.data.Meal

@Composable
fun RecipeDetailScreen(
    meal: Meal?,
    isLoading: Boolean,
    errorMessage: String?,
    timerSecondsRemaining: Int,
    isTimerRunning: Boolean,
    onStartTimer: (Int) -> Unit,
    onStopTimer: () -> Unit,
    onBackClick: () -> Unit,
    onAddToShoppingList: (String) -> Unit
) {
    var showTimerDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
            errorMessage != null -> {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Error loading recipe", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(16.dp))
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            meal != null -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = onBackClick) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.primary)
                                }
                                IconButton(onClick = {
                                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_SUBJECT, meal.strMeal)
                                        putExtra(Intent.EXTRA_TEXT, "Check out this recipe: ${meal.strMeal}\n\nIngredients:\n${meal.getIngredientsList().joinToString("\n") { "• ${it.second} ${it.first}" }}")
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, "Share Recipe"))
                                }) {
                                    Icon(Icons.Default.Share, contentDescription = "Share", tint = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }

                        item {
                            AsyncImage(
                                model = meal.strMealThumb,
                                contentDescription = meal.strMeal,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                                    .padding(horizontal = 16.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }

                        item {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = meal.strMeal,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (!meal.strCategory.isNullOrEmpty()) {
                                        Text(text = meal.strCategory, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    Button(
                                        onClick = { showTimerDialog = true },
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                        modifier = Modifier.height(32.dp)
                                    ) {
                                        Icon(Icons.Default.Timer, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(Modifier.width(4.dp))
                                        Text("Timer", fontSize = 12.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                Text(text = "Ingredients", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.height(12.dp))

                                meal.getIngredientsList().forEach { (ingredient, measure) ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = "• $measure $ingredient", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
                                        IconButton(
                                            onClick = { onAddToShoppingList("$measure $ingredient") },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(Icons.Default.AddShoppingCart, contentDescription = "Add to Cart", tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f), modifier = Modifier.size(18.dp))
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                Text(text = "Instructions", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(text = meal.strInstructions ?: "No instructions available", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)

                                Spacer(modifier = Modifier.height(120.dp))
                            }
                        }
                    }

                    // Timer Overlay
                    if (isTimerRunning || timerSecondsRemaining > 0) {
                        Surface(
                            modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp).padding(bottom = 16.dp).fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            tonalElevation = 8.dp
                        ) {
                            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                                Column {
                                    Text("Cooking Timer", style = MaterialTheme.typography.labelSmall)
                                    Text(text = formatTime(timerSecondsRemaining), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                                }
                                if (isTimerRunning) {
                                    IconButton(onClick = onStopTimer) { Icon(Icons.Default.Stop, contentDescription = "Stop", tint = MaterialTheme.colorScheme.error) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showTimerDialog) {
        var minutesInput by remember { mutableStateOf("5") }
        AlertDialog(
            onDismissRequest = { showTimerDialog = false },
            title = { Text("Set Timer") },
            text = {
                TextField(
                    value = minutesInput,
                    onValueChange = { if (it.all { c -> c.isDigit() }) minutesInput = it },
                    label = { Text("Minutes") },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    onStartTimer(minutesInput.toIntOrNull() ?: 5)
                    showTimerDialog = false
                }) { Text("Start") }
            },
            dismissButton = { TextButton(onClick = { showTimerDialog = false }) { Text("Cancel") } }
        )
    }
}

private fun formatTime(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return "%02d:%02d".format(mins, secs)
}
