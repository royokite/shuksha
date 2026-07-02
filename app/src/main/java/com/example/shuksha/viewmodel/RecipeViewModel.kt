package com.example.shuksha.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.shuksha.data.Meal
import com.example.shuksha.data.AreaItem
import com.example.shuksha.data.CategoryItem
import com.example.shuksha.navigation.NavItem
import com.example.shuksha.repository.RecipeRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class RecipeViewModel: ViewModel() {

    private val repository = RecipeRepository()

    var isReady by mutableStateOf(false)
        private set

    // Navigation
    var currentNavItem by mutableStateOf(NavItem.HOME)
        private set

    // Home screen
    var latestMeals by mutableStateOf<List<Meal>>(emptyList())
        private set

    // Search screen
    var meals by mutableStateOf<List<Meal>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var searchQuery by mutableStateOf("")
        private set

    // Detail screen
    var selectedMeal by mutableStateOf<Meal?>(null)
        private set

    var detailIsLoading by mutableStateOf(false)
        private set

    var detailErrorMessage by mutableStateOf<String?>(null)
        private set

    // Explore screen
    var randomMeals by mutableStateOf<List<Meal>>(emptyList())
        private set

    // Browse screen
    var categories by mutableStateOf<List<CategoryItem>>(emptyList())
        private set

    var areas by mutableStateOf<List<AreaItem>>(emptyList())
        private set

    var isLoadingCategories by mutableStateOf(false)
        private set

    var isLoadingAreas by mutableStateOf(false)
        private set

    // Browse by letter
    var browseByLetterResults by mutableStateOf<List<Meal>>(emptyList())
        private set

    var selectedLetter by mutableStateOf("")
        private set

    var browseCategoryResults by mutableStateOf<List<Meal>>(emptyList())
        private set

    var selectedCategory by mutableStateOf("")
        private set

    var browseAreaResults by mutableStateOf<List<Meal>>(emptyList())
        private set

    var selectedArea by mutableStateOf("")
        private set

    init {
        initialLoad()
    }

    private fun initialLoad() {
        viewModelScope.launch {
            coroutineScope {
                val latestTask = async { loadLatestMeals() }
                val categoriesTask = async { loadCategoriesAndAreas() }
                latestTask.await()
                categoriesTask.await()
            }
            isReady = true
        }
    }

    fun navigateTo(item: NavItem) {
        currentNavItem = item
        when (item) {
            NavItem.HOME -> if (latestMeals.isEmpty()) viewModelScope.launch { loadLatestMeals() }
            NavItem.EXPLORE -> if (randomMeals.isEmpty()) loadRandomMeal()
            NavItem.SEARCH -> {}
            NavItem.BROWSE -> if (categories.isEmpty()) viewModelScope.launch {loadCategoriesAndAreas()}
        }
    }

    private suspend fun loadLatestMeals() {
        isLoading = true
        errorMessage = null
        try {
            latestMeals = repository.searchRecipes("a")
        } catch (e: Exception) {
            errorMessage = e.message
        } finally {
            isLoading = false
        }
    }

    private fun loadRandomMeal() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val newMeal = repository.getRandomMeal()
                randomMeals = randomMeals + newMeal
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    private suspend fun loadCategoriesAndAreas() {
        try {
            isLoadingCategories = true
            categories = repository.getCategoryList()
        } catch (e: Exception) {
            errorMessage = e.message
        } finally {
            isLoadingCategories = false
        }

        try {
            isLoadingAreas = true
            areas = repository.getAreaList()
        } catch (e: Exception) {
            errorMessage = e.message
        } finally {
            isLoadingAreas = false
        }
    }

    fun onSearchQueryChange(query: String) {
        searchQuery = query
    }

    fun performSearch() {
        loadRecipes(searchQuery)
    }

    private fun loadRecipes(query: String = "beef") {
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

    fun loadRecipesByCategory(category: String) {
        selectedCategory = category
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                browseCategoryResults = repository.getMealsByCategory(category)
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun loadRecipesByArea(area: String) {
        selectedArea = area
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                browseAreaResults = repository.getMealsByArea(area)
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun loadRecipesByFirstLetter(letter: String) {
        selectedLetter = letter
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                browseByLetterResults = repository.getMealsByFirstLetter(letter)
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun clearBrowseResults() {
        browseByLetterResults = emptyList()
        browseCategoryResults = emptyList()
        browseAreaResults = emptyList()
        selectedLetter = ""
        selectedCategory = ""
        selectedArea = ""
    }
}