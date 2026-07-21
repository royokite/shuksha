package com.example.shuksha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shuksha.navigation.NavItem
import com.example.shuksha.presentation.*
import com.example.shuksha.ui.ShukshaTheme
import com.example.shuksha.viewmodel.RecipeViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            ShukshaTheme {
                val vm: RecipeViewModel = viewModel()
                val favoriteIds by vm.favoriteIds.collectAsState()
                val favoriteMeals by vm.favorites.collectAsState()
                val shoppingList by vm.shoppingList.collectAsState()
                val customRecipes by vm.customRecipes.collectAsState()
                val snackbarHostState = remember { SnackbarHostState() }

                // Collect UI events (notifications for favorites, timer, etc)
                LaunchedEffect(Unit) {
                    vm.uiEvent.collectLatest { message ->
                        snackbarHostState.showSnackbar(message)
                    }
                }

                // Handle Splash Screen Animation
                var showMainContent by remember { mutableStateOf(false) }

                if (!showMainContent) {
                    AnimatedSplashScreen(
                        onAnimationFinished = { showMainContent = true },
                        isReady = vm.isReady
                    )
                } else {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = MaterialTheme.colorScheme.background,
                        snackbarHost = {
                            SnackbarHost(snackbarHostState) { data ->
                                Snackbar(
                                    snackbarData = data,
                                    containerColor = Color(0xFFE8741B), // Themed orange
                                    contentColor = Color.White,
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }
                        },
                        bottomBar = {
                            BottomNavBar(
                                currentItem = vm.currentNavItem,
                                onItemSelected = { 
                                    vm.clearSelection() 
                                    vm.clearBrowseResults()
                                    vm.navigateTo(it) 
                                }
                            )
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = innerPadding.calculateBottomPadding())
                        ) {
                            if (vm.selectedMeal != null) {
                                RecipeDetailScreen(
                                    meal = vm.selectedMeal,
                                    isLoading = vm.detailIsLoading,
                                    errorMessage = vm.detailErrorMessage,
                                    timerSecondsRemaining = vm.timerSecondsRemaining,
                                    isTimerRunning = vm.isTimerRunning,
                                    onStartTimer = { vm.startTimer(it) },
                                    onStopTimer = { vm.stopTimer() },
                                    onBackClick = { vm.clearSelection() },
                                    onAddToShoppingList = { vm.addToShoppingList(it) }
                                )
                            } else {
                                when (vm.currentNavItem) {
                                    NavItem.HOME -> HomeScreen(
                                        latestMeals = vm.latestMeals,
                                        isLoading = vm.isLoading,
                                        errorMessage = vm.errorMessage,
                                        favoriteIds = favoriteIds,
                                        onFavoriteClick = { vm.toggleFavorite(it) },
                                        onMealClick = { vm.selectMeal(it) }
                                    )
                                    NavItem.SEARCH -> RecipeScreen(
                                        meals = vm.meals,
                                        isLoading = vm.isLoading,
                                        errorMessage = vm.errorMessage,
                                        searchQuery = vm.searchQuery,
                                        onSearchQueryChange = { vm.onSearchQueryChange(it) },
                                        onSearchSubmit = { vm.performSearch() },
                                        favoriteIds = favoriteIds,
                                        onFavoriteClick = { vm.toggleFavorite(it) },
                                        onMealClick = { vm.selectMeal(it) }
                                    )
                                    NavItem.BROWSE -> {
                                        if (vm.browseByLetterResults.isNotEmpty() || vm.selectedLetter.isNotEmpty()) {
                                            BrowseByLetterResultsScreen(
                                                meals = vm.browseByLetterResults,
                                                isLoading = vm.isLoading,
                                                errorMessage = vm.errorMessage,
                                                selectedLetter = vm.selectedLetter,
                                                favoriteIds = favoriteIds,
                                                onFavoriteClick = { vm.toggleFavorite(it) },
                                                onMealClick = { vm.selectMeal(it) },
                                                onBackClick = { vm.clearBrowseResults() }
                                            )
                                        } else if (vm.selectedCategory.isNotEmpty()) {
                                            RecipeScreen(
                                                meals = vm.browseCategoryResults,
                                                isLoading = vm.isLoading,
                                                errorMessage = vm.errorMessage,
                                                searchQuery = vm.selectedCategory,
                                                onSearchQueryChange = {},
                                                onSearchSubmit = {},
                                                favoriteIds = favoriteIds,
                                                onFavoriteClick = { vm.toggleFavorite(it) },
                                                onMealClick = { vm.selectMeal(it) },
                                                showBackButton = true,
                                                onBackClick = { vm.clearBrowseResults() }
                                            )
                                        } else if (vm.selectedArea.isNotEmpty()) {
                                            RecipeScreen(
                                                meals = vm.browseAreaResults,
                                                isLoading = vm.isLoading,
                                                errorMessage = vm.errorMessage,
                                                searchQuery = vm.selectedArea,
                                                onSearchQueryChange = {},
                                                onSearchSubmit = {},
                                                favoriteIds = favoriteIds,
                                                onFavoriteClick = { vm.toggleFavorite(it) },
                                                onMealClick = { vm.selectMeal(it) },
                                                showBackButton = true,
                                                onBackClick = { vm.clearBrowseResults() }
                                            )
                                        } else {
                                            BrowseScreen(
                                                categories = vm.categories,
                                                areas = vm.areas,
                                                isLoadingCategories = vm.isLoadingCategories,
                                                isLoadingAreas = vm.isLoadingAreas,
                                                errorMessage = vm.errorMessage,
                                                onCategoryClick = { vm.loadRecipesByCategory(it) },
                                                onAreaClick = { vm.loadRecipesByArea(it) },
                                                onLetterClick = { vm.loadRecipesByFirstLetter(it) }
                                            )
                                        }
                                    }
                                    NavItem.SHOPPING -> ShoppingListScreen(
                                        shoppingItems = shoppingList,
                                        onToggleItem = { vm.toggleShoppingItem(it) },
                                        onRemoveItem = { vm.removeShoppingItem(it) },
                                        onClearCompleted = { vm.clearCompletedShopping() }
                                    )
                                    NavItem.PROFILE -> ProfileScreen(
                                        isLoggedIn = vm.isLoggedIn,
                                        userEmail = vm.userEmail,
                                        favoriteMeals = favoriteMeals,
                                        customRecipes = customRecipes,
                                        onLogin = { vm.login(it) },
                                        onLogout = { vm.logout() },
                                        onSaveCustomRecipe = { t, c, ing, ins -> vm.saveCustomRecipe(t, c, ing, ins) },
                                        onMealClick = { vm.selectMeal(it) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedSplashScreen(onAnimationFinished: () -> Unit, isReady: Boolean) {
    var startAnimation by remember { mutableStateOf(false) }
    
    // Pulse Animation
    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1.2f else 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    // Fade Out Animation
    val alpha by animateFloatAsState(
        targetValue = if (isReady) 0f else 1f,
        animationSpec = tween(600),
        label = "fade",
        finishedListener = { if (isReady) onAnimationFinished() }
    )

    LaunchedEffect(Unit) { startAnimation = true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .alpha(alpha),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.mipmap.ic_launcher_foreground),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(140.dp)
                .scale(scale)
        )
    }
}
