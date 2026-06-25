package com.example.shuksha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shuksha.navigation.NavItem
import com.example.shuksha.presentation.BrowseByLetterResultsScreen
import com.example.shuksha.presentation.BrowseScreen
import com.example.shuksha.presentation.BottomNavBar
import com.example.shuksha.presentation.ExploreScreen
import com.example.shuksha.presentation.HomeScreen
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

                Column(modifier = androidx.compose.ui.Modifier.fillMaxSize()) {
                    Box(
                        modifier = androidx.compose.ui.Modifier.weight(1f)
                    ) {
                        // Show detail screen if a meal is selected
                        if (vm.selectedMeal != null) {
                            RecipeDetailScreen(
                                meal = vm.selectedMeal,
                                isLoading = vm.detailIsLoading,
                                errorMessage = vm.detailErrorMessage,
                                onBackClick = { vm.clearSelection() }
                            )
                        } else {
                            // Show navigation-based screens
                            when (vm.currentNavItem) {
                                NavItem.HOME -> {
                                    HomeScreen(
                                        latestMeals = vm.latestMeals,
                                        isLoading = vm.isLoading,
                                        errorMessage = vm.errorMessage,
                                        onMealClick = { vm.selectMeal(it) }
                                    )
                                }

                                NavItem.EXPLORE -> {
                                    ExploreScreen(
                                        randomMeals = vm.randomMeals,
                                        isLoading = vm.isLoading,
                                        errorMessage = vm.errorMessage,
                                        onMealClick = { vm.selectMeal(it) },
                                        onLoadMore = { vm.navigateTo(NavItem.EXPLORE) }
                                    )
                                }

                                NavItem.SEARCH -> {
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

                                NavItem.BROWSE -> {
                                    if (vm.browseByLetterResults.isNotEmpty()) {
                                        BrowseByLetterResultsScreen(
                                            meals = vm.browseByLetterResults,
                                            isLoading = vm.isLoading,
                                            errorMessage = vm.errorMessage,
                                            selectedLetter = vm.selectedLetter,
                                            onMealClick = { vm.selectMeal(it) },
                                            onBackClick = { vm.clearBrowseResults() }
                                        )
                                    } else if (vm.browseCategoryResults.isNotEmpty()) {
                                        RecipeScreen(
                                            meals = vm.browseCategoryResults,
                                            isLoading = vm.isLoading,
                                            errorMessage = vm.errorMessage,
                                            searchQuery = vm.selectedCategory,
                                            onSearchQueryChange = {},
                                            onSearchSubmit = {},
                                            onMealClick = { vm.selectMeal(it) }
                                        )
                                    } else if (vm.browseAreaResults.isNotEmpty()) {
                                        RecipeScreen(
                                            meals = vm.browseAreaResults,
                                            isLoading = vm.isLoading,
                                            errorMessage = vm.errorMessage,
                                            searchQuery = vm.selectedArea,
                                            onSearchQueryChange = {},
                                            onSearchSubmit = {},
                                            onMealClick = { vm.selectMeal(it) }
                                        )
                                    } else {
                                        BrowseScreen(
                                            categories = vm.categories,
                                            areas = vm.areas,
                                            isLoadingCategories = vm.isLoadingCategories,
                                            isLoadingAreas = vm.isLoadingAreas,
                                            errorMessage = vm.errorMessage,
                                            onCategoryClick = { vm.loadRecipesByCategory(it) },
                                            onAreaClick = { vm.loadRecipesByArea(it) }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Bottom Navigation Bar
                    BottomNavBar(
                        currentItem = vm.currentNavItem,
                        onItemSelected = { vm.navigateTo(it) }
                    )
                }
            }
        }
    }
}