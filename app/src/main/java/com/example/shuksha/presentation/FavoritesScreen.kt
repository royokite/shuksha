package com.example.shuksha.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shuksha.data.FavoriteMeal
import com.example.shuksha.data.Meal

@Composable
fun FavoritesScreen(
    favoriteMeals: List<FavoriteMeal>,
    onFavoriteClick: (Meal) -> Unit,
    onMealClick: (Meal) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        if (favoriteMeals.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No favorite recipes yet!",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        text = "My Favorites",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 24.sp,
                        modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)
                    )
                }

                items(favoriteMeals) { favMeal ->
                    val meal = Meal(
                        idMeal = favMeal.idMeal,
                        strMeal = favMeal.strMeal,
                        strMealThumb = favMeal.strMealThumb,
                        strCategory = favMeal.strCategory,
                        strArea = favMeal.strArea,
                        strInstructions = null,
                        strTags = null,
                        strYoutube = null,
                        strIngredient1 = null, strIngredient2 = null, strIngredient3 = null, strIngredient4 = null, strIngredient5 = null,
                        strIngredient6 = null, strIngredient7 = null, strIngredient8 = null, strIngredient9 = null, strIngredient10 = null,
                        strIngredient11 = null, strIngredient12 = null, strIngredient13 = null, strIngredient14 = null, strIngredient15 = null,
                        strIngredient16 = null, strIngredient17 = null, strIngredient18 = null, strIngredient19 = null, strIngredient20 = null,
                        strMeasure1 = null, strMeasure2 = null, strMeasure3 = null, strMeasure4 = null, strMeasure5 = null,
                        strMeasure6 = null, strMeasure7 = null, strMeasure8 = null, strMeasure9 = null, strMeasure10 = null,
                        strMeasure11 = null, strMeasure12 = null, strMeasure13 = null, strMeasure14 = null, strMeasure15 = null,
                        strMeasure16 = null, strMeasure17 = null, strMeasure18 = null, strMeasure19 = null, strMeasure20 = null,
                        strSource = null, strImageSource = null, strCreativeCommonsConfirmed = null, dateModified = null
                    )
                    RecipeCard(
                        meal = meal,
                        isFavorite = true,
                        onFavoriteClick = onFavoriteClick,
                        onClick = { onMealClick(meal) }
                    )
                }
            }
        }
    }
}
