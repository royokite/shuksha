package com.example.shuksha.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.shuksha.data.*
import com.example.shuksha.navigation.NavItem
import com.example.shuksha.repository.RecipeRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RecipeRepository()
    private val database = AppDatabase.getDatabase(application)
    private val favoriteDao = database.favoriteMealDao()
    private val shoppingDao = database.shoppingDao()
    private val customRecipeDao = database.customRecipeDao()

    var isReady by mutableStateOf(false)
        private set

    // UI Events (for Snackbars/Toasts)
    private val _uiEvent = MutableSharedFlow<String>()
    val uiEvent: SharedFlow<String> = _uiEvent.asSharedFlow()

    // Auth state (Simplified for now)
    var isLoggedIn by mutableStateOf(false)
        private set
    var userEmail by mutableStateOf("")
        private set

    // Navigation
    var currentNavItem by mutableStateOf(NavItem.HOME)
        private set

    // Data states
    var latestMeals by mutableStateOf<List<Meal>>(emptyList())
        private set
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

    // Explore & Browse
    var randomMeals by mutableStateOf<List<Meal>>(emptyList())
        private set
    var categories by mutableStateOf<List<CategoryItem>>(emptyList())
        private set
    var areas by mutableStateOf<List<AreaItem>>(emptyList())
        private set
    var isLoadingCategories by mutableStateOf(false)
        private set
    var isLoadingAreas by mutableStateOf(false)
        private set

    // Browse drill-down
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

    // Favorites
    val favorites: StateFlow<List<FavoriteMeal>> = favoriteDao.getAllFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteIds: StateFlow<Set<String>> = favorites.map { list -> list.map { it.idMeal }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    // Shopping List
    val shoppingList: StateFlow<List<ShoppingItem>> = shoppingDao.getAllItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Custom Recipes
    val customRecipes: StateFlow<List<CustomRecipe>> = customRecipeDao.getAllCustomRecipes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Timer logic
    var timerSecondsRemaining by mutableStateOf(0)
    var isTimerRunning by mutableStateOf(false)
        private set

    init {
        initialLoad()
    }

    private fun initialLoad() {
        viewModelScope.launch {
            try {
                coroutineScope {
                    val latestTask = async { loadLatestMealsInternal() }
                    val categoriesTask = async { loadCategoriesAndAreasInternal() }
                    latestTask.await()
                    categoriesTask.await()
                }
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isReady = true
            }
        }
    }

    fun login(email: String) {
        userEmail = email
        isLoggedIn = true
        viewModelScope.launch { _uiEvent.emit("Logged in as $email") }
    }

    fun logout() {
        isLoggedIn = false
        userEmail = ""
        viewModelScope.launch { _uiEvent.emit("Logged out") }
    }

    fun navigateTo(item: NavItem) {
        currentNavItem = item
        when (item) {
            NavItem.HOME -> if (latestMeals.isEmpty() && !isLoading) {
                viewModelScope.launch { loadLatestMealsInternal() }
            }
            NavItem.BROWSE -> if (categories.isEmpty() && !isLoadingCategories) {
                viewModelScope.launch { loadCategoriesAndAreasInternal() }
            }
            else -> {}
        }
    }

    private suspend fun loadLatestMealsInternal() {
        if (isLoading) return
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

    fun loadRandomMeal() {
        if (isLoading) return
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

    private suspend fun loadCategoriesAndAreasInternal() {
        if (isLoadingCategories) return
        try {
            isLoadingCategories = true
            categories = repository.getCategoryList()
            areas = repository.getAreaList()
        } catch (e: Exception) {
            errorMessage = e.message
        } finally {
            isLoadingCategories = false
        }
    }

    fun onSearchQueryChange(query: String) {
        searchQuery = query
    }

    fun performSearch() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                meals = repository.searchRecipes(searchQuery)
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

    fun toggleFavorite(meal: Meal) {
        viewModelScope.launch {
            val isFav = favoriteDao.isFavorite(meal.idMeal)
            val favMeal = FavoriteMeal(
                idMeal = meal.idMeal,
                strMeal = meal.strMeal,
                strMealThumb = meal.strMealThumb,
                strCategory = meal.strCategory,
                strArea = meal.strArea
            )
            if (isFav) {
                favoriteDao.deleteFavorite(favMeal)
                _uiEvent.emit("Removed from favorites")
            } else {
                favoriteDao.insertFavorite(favMeal)
                _uiEvent.emit("Added to favorites")
            }
        }
    }

    // Shopping List Actions
    fun addToShoppingList(ingredient: String) {
        viewModelScope.launch {
            shoppingDao.insertItem(ShoppingItem(name = ingredient))
            _uiEvent.emit("Added $ingredient to shopping list")
        }
    }

    fun toggleShoppingItem(item: ShoppingItem) {
        viewModelScope.launch {
            shoppingDao.updateItem(item.copy(isChecked = !item.isChecked))
        }
    }

    fun removeShoppingItem(item: ShoppingItem) {
        viewModelScope.launch {
            shoppingDao.deleteItem(item)
        }
    }

    fun clearCompletedShopping() {
        viewModelScope.launch {
            shoppingDao.clearCompleted()
        }
    }

    // Custom Recipe Actions
    fun saveCustomRecipe(title: String, category: String, ingredients: String, instructions: String) {
        viewModelScope.launch {
            customRecipeDao.insertRecipe(CustomRecipe(
                title = title,
                category = category,
                ingredients = ingredients,
                instructions = instructions
            ))
            _uiEvent.emit("Recipe '$title' saved!")
        }
    }

    // Timer logic
    fun startTimer(minutes: Int) {
        stopTimer()
        timerSecondsRemaining = minutes * 60
        isTimerRunning = true
        viewModelScope.launch {
            while (timerSecondsRemaining > 0 && isTimerRunning) {
                delay(1000)
                if (isTimerRunning) timerSecondsRemaining--
            }
            if (timerSecondsRemaining == 0 && isTimerRunning) {
                isTimerRunning = false
                _uiEvent.emit("Timer finished!")
            }
        }
    }

    fun stopTimer() {
        isTimerRunning = false
        timerSecondsRemaining = 0
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
