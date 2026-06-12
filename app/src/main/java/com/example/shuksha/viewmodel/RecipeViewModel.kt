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

    fun loadRecipes() {
        viewModelScope.launch {
            try {
                meals = repository.searchRecipes("chicken")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}