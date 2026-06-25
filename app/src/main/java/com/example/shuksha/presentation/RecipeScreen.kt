package com.example.shuksha.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.shuksha.data.Meal

@Composable
fun RecipeScreen(
    meals: List<Meal>,
    isLoading: Boolean,
    errorMessage: String?
) {

    when {
        isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        errorMessage != null -> {
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center) {
                Text(
                    text = "Error: $errorMessage"
                )
            }
        }

        meals.isEmpty() -> {
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center) {
                Text(
                    text = "No Recipes Found!"
                )
            }
        }

        else -> {
            LazyColumn {
                items(meals) {meal -> RecipeCard(meal)}
            }

        }
    }
}