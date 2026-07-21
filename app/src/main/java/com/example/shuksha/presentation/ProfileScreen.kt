package com.example.shuksha.presentation

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shuksha.data.CustomRecipe
import com.example.shuksha.data.FavoriteMeal
import com.example.shuksha.data.Meal

@Composable
fun ProfileScreen(
    isLoggedIn: Boolean,
    userEmail: String,
    favoriteMeals: List<FavoriteMeal>,
    customRecipes: List<CustomRecipe>,
    onLogin: (String) -> Unit,
    onLogout: () -> Unit,
    onSaveCustomRecipe: (String, String, String, String) -> Unit,
    onMealClick: (Meal) -> Unit
) {
    var showCreateRecipe by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        if (!isLoggedIn) {
            LoginView(onLogin = onLogin)
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    ProfileHeader(email = userEmail, onLogout = onLogout)
                }

                item {
                    SectionHeader(
                        title = "My Recipes", 
                        actionText = "Add New", 
                        onAction = { showCreateRecipe = true }
                    )
                }

                if (customRecipes.isEmpty()) {
                    item {
                        Text(
                            "You haven't created any recipes yet.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    items(customRecipes) { recipe ->
                        CustomRecipeItem(recipe)
                    }
                }

                item {
                    SectionHeader(title = "Recent Favorites", onAction = {})
                }

                if (favoriteMeals.isEmpty()) {
                    item {
                        Text(
                            "No favorites saved.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    items(favoriteMeals.take(3)) { favMeal ->
                        val meal = Meal(
                            idMeal = favMeal.idMeal,
                            strMeal = favMeal.strMeal,
                            strMealThumb = favMeal.strMealThumb,
                            strCategory = favMeal.strCategory,
                            strArea = favMeal.strArea
                        )
                        RecipeCard(
                            meal = meal,
                            isFavorite = true,
                            onClick = { onMealClick(meal) }
                        )
                    }
                }
                
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }

    if (showCreateRecipe) {
        CreateRecipeDialog(
            onDismiss = { showCreateRecipe = false },
            onSave = { title, cat, ing, ins ->
                onSaveCustomRecipe(title, cat, ing, ins)
                showCreateRecipe = false
            }
        )
    }
}

@Composable
fun ProfileHeader(email: String, onLogout: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person, 
                    contentDescription = null, 
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
            }
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = "Welcome back!",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = email,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        IconButton(onClick = onLogout) {
            Icon(Icons.Default.Logout, contentDescription = "Logout", tint = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
fun LoginView(onLogin: (String) -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.AccountCircle,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            "Shuksha Account",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        Text(
            "Login to save your own recipes and sync your favorites.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(32.dp))
        
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = { if (email.isNotEmpty()) onLogin(email) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Sign In")
        }
    }
}

@Composable
fun SectionHeader(title: String, actionText: String? = null, onAction: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        if (actionText != null) {
            TextButton(onClick = onAction) {
                Text(actionText)
            }
        }
    }
}

@Composable
fun CustomRecipeItem(recipe: CustomRecipe) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Restaurant, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(recipe.title, fontWeight = FontWeight.Bold)
                Text(recipe.category, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
fun CreateRecipeDialog(onDismiss: () -> Unit, onSave: (String, String, String, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Recipe") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                TextField(value = category, onValueChange = { category = it }, label = { Text("Category") })
                TextField(value = ingredients, onValueChange = { ingredients = it }, label = { Text("Ingredients") }, minLines = 3)
                TextField(value = instructions, onValueChange = { instructions = it }, label = { Text("Instructions") }, minLines = 3)
            }
        },
        confirmButton = {
            TextButton(onClick = { if (title.isNotEmpty()) onSave(title, category, ingredients, instructions) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
