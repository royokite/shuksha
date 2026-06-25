package com.example.shuksha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shuksha.presentation.RecipeDetailScreen
import com.example.shuksha.presentation.RecipeScreen
import com.example.shuksha.ui.ShukshaTheme
import com.example.shuksha.viewmodel.RecipeViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        setContent {
            ShukshaTheme {
                val vm: RecipeViewModel = viewModel()

                LaunchedEffect(Unit) {
                    vm.loadRecipes()
                }

                if (vm.selectedMeal != null) {
                    RecipeDetailScreen(
                        meal = vm.selectedMeal,
                        isLoading = vm.detailIsLoading,
                        errorMessage = vm.detailErrorMessage,
                        onBackClick = { vm.clearSelection() }
                    )
                } else {
                    RecipeScreen(
                        meals = vm.meals,
                        isLoading = vm.isLoading,
                        errorMessage = vm.errorMessage,
                        searchQuery = vm.searchQuery,
                        onSearchQueryChange = { vm.onSearchQueryChange(it) },
                        onSearchSubmit = { vm.performSearch() },
                        onMealClick = { vm.selectMeal(it) }
                    )
                }
            }
        }
    }
}