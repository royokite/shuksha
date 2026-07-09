package com.example.shuksha.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
    onBackClick: () -> Unit
) {
    var showTimerDialog by remember { mutableStateOf(false) }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
    ) {
        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }

            errorMessage != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Error loading recipe",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            meal != null -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        item {
                            IconButton(onClick = onBackClick, modifier = Modifier.padding(8.dp)) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        item {
                            AsyncImage(
                                model = meal.strMealThumb,
                                contentDescription = meal.strMeal,
                                modifier =
                                    Modifier
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
                                        Text(
                                            text = meal.strCategory,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    if (!meal.strArea.isNullOrEmpty()) {
                                        Text(
                                            text = meal.strArea,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.weight(1f))

                                    // Timer Trigger Button
                                    Button(
                                        onClick = { showTimerDialog = true },
                                        contentPadding =
                                            PaddingValues(
                                                horizontal = 12.dp,
                                                vertical = 4.dp
                                            ),
                                        modifier = Modifier.height(32.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Timer,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(Modifier.width(4.dp))
                                        Text("Timer", fontSize = 12.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "Ingredients",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                meal.getIngredientsList().forEach { (ingredient, measure) ->
                                    Text(
                                        text = "• $measure $ingredient",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "Instructions",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = meal.strInstructions ?: "No instructions available",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(
                                    modifier = Modifier.height(100.dp)
                                ) // Extra space for bottom bar/timer
                            }
                        }
                    }

                    // Floating Timer Overlay
                    if (isTimerRunning || timerSecondsRemaining > 0) {
                        Surface(
                            modifier =
                                Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFFE8741B),
                            tonalElevation = 8.dp
                        ) {
                            Row(
                                modifier =
                                    Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        "Recipe Timer",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White.copy(alpha = 0.8f)
                                    )
                                    Text(
                                        text = formatTime(timerSecondsRemaining),
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                                if (isTimerRunning) {
                                    IconButton(onClick = onStopTimer) {
                                        Icon(
                                            Icons.Default.Stop,
                                            contentDescription = "Stop timer",
                                            tint = Color.White,
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }
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
            title = { Text("Set Cooking Timer", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "Quick select:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf(5, 10, 15, 20, 30).forEach { min ->
                            FilterChip(
                                selected = minutesInput == min.toString(),
                                onClick = { minutesInput = min.toString() },
                                label = {
                                    Text(
                                        "${min}",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                },
                                modifier = Modifier.weight(1f),
                                colors =
                                    FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(0xFFE8741B),
                                        selectedLabelColor = Color.White
                                    )
                            )
                        }
                    }
                    OutlinedTextField(
                        value = minutesInput,
                        onValueChange = { v ->
                            if (v.all { it.isDigit() } && v.length <= 3) minutesInput = v
                        },
                        keyboardOptions =
                            KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        label = { Text("Custom minutes") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val mins = minutesInput.toIntOrNull()?.coerceIn(1, 999) ?: 5
                        onStartTimer(mins)
                        showTimerDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE8741B))
                ) { Text("Start Timer", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { showTimerDialog = false }) { Text("Cancel") }
            }
        )
    }
}

private fun formatTime(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return "%02d:%02d".format(mins, secs)
}
