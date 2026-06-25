package com.example.shuksha.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
fun BrowseByLetterResultsScreen(
        meals: List<Meal>,
        isLoading: Boolean,
        errorMessage: String?,
        selectedLetter: String,
        onMealClick: (Meal) -> Unit,
        onBackClick: () -> Unit
) {
    if (isLoading && meals.isEmpty()) {
        Box(
                modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
        ) { CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) }
    } else if (errorMessage != null) {
        Column(
                modifier =
                        Modifier.fillMaxSize()
                                .background(MaterialTheme.colorScheme.background)
                                .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Error loading meals", color = MaterialTheme.colorScheme.error)
            Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
            )
        }
    } else {
        LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier =
                        Modifier.fillMaxSize()
                                .background(MaterialTheme.colorScheme.background)
                                .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
        ) { items(meals) { meal -> RecipeCard(meal) { onMealClick(meal) } } }
    }
}

@Composable
fun BrowseByLetterScreen(onLetterSelected: (String) -> Unit) {
    val alphabet = ('A'..'Z').map { it.toString() }

    Column(
            modifier =
                    Modifier.fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(16.dp)
    ) {
        Text(
                text = "Browse by Letter",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 24.sp
        )

        LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(alphabet) { letter ->
                BrowseItem(text = letter, onClick = { onLetterSelected(letter) })
            }
        }
    }
}
