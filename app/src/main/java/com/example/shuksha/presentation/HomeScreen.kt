package com.example.shuksha.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shuksha.data.Meal

@Composable
fun HomeScreen(
    latestMeals: List<Meal>,
    isLoading: Boolean,
    errorMessage: String?,
    onMealClick: (Meal) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        item {
            Text(
                text = "Latest Recipes",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        when {
            isLoading -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(300.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            errorMessage != null -> {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(300.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Error loading meals",
                            color = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            latestMeals.isEmpty() -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(300.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "No meals available",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            else -> {
                items(latestMeals.size) { index ->
                    RecipeCard(latestMeals[index]) {
                        onMealClick(latestMeals[index])
                    }
                    if (index < latestMeals.size - 1) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}
