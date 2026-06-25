package com.example.shuksha.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.shuksha.data.Meal
import com.example.shuksha.repository.RecipeRepository

class RecipeViewModel: ViewModel() {

    private val repository = RecipeRepository()

    var meals by mutableStateOf<List<Meal>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var searchQuery by mutableStateOf("")
        private set

    var selectedMeal by mutableStateOf<Meal?>(null)
        private set

    var detailIsLoading by mutableStateOf(false)
        private set

    var detailErrorMessage by mutableStateOf<String?>(null)
        private set

    fun loadRecipes(query: String = "beef") {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                meals = repository.searchRecipes(query)
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        searchQuery = query
    }

    fun performSearch() {
        if (searchQuery.isNotEmpty()) {
            loadRecipes(searchQuery)
        }
    }

    fun selectMeal(meal: Meal) {
        selectedMeal = meal
        loadMealDetails(meal.idMeal)
    }

    fun clearSelection() {
        selectedMeal = null
        detailErrorMessage = null
    }

    private fun loadMealDetails(mealId: String) {
        viewModelScope.launch {
            detailIsLoading = true
            detailErrorMessage = null

            try {
                val response = repository.getMealDetails(mealId)
                selectedMeal = response.firstOrNull() ?: selectedMeal
            } catch (e: Exception) {
                detailErrorMessage = e.message
            } finally {
                detailIsLoading = false
            }
        }
    }
}