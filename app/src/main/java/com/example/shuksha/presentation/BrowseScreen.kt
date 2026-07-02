package com.example.shuksha.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shuksha.data.AreaItem
import com.example.shuksha.data.CategoryItem

@Composable
fun BrowseScreen(
    categories: List<CategoryItem>,
    areas: List<AreaItem>,
    isLoadingCategories: Boolean,
    isLoadingAreas: Boolean,
    errorMessage: String?,
    onCategoryClick: (String) -> Unit,
    onAreaClick: (String) -> Unit,
    onLetterClick: (String) -> Unit
) {
    var categoriesExpanded by remember { mutableStateOf(true) }
    var cuisineExpanded by remember { mutableStateOf(false) }
    var lettersExpanded by remember { mutableStateOf(false) }

    val alphabet = ('A'..'Z').map { it.toString() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Title
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "Browse Recipes",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            // Categories Section Header
            item(span = { GridItemSpan(maxLineSpan) }) {
                CollapsibleHeader(
                    title = "By Category",
                    isExpanded = categoriesExpanded,
                    onToggle = { categoriesExpanded = !categoriesExpanded }
                )
            }

            if (categoriesExpanded) {
                if (isLoadingCategories) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        LoadingIndicator()
                    }
                } else if (errorMessage != null) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        ErrorText(message = "Error loading categories")
                    }
                } else {
                    items(categories) { category ->
                        BrowseItem(
                            text = category.strCategory,
                            onClick = { onCategoryClick(category.strCategory) }
                        )
                    }
                }
            }

            // Cuisine Section Header
            item(span = { GridItemSpan(maxLineSpan) }) {
                Spacer(modifier = Modifier.height(8.dp))
                CollapsibleHeader(
                    title = "By Cuisine",
                    isExpanded = cuisineExpanded,
                    onToggle = { cuisineExpanded = !cuisineExpanded }
                )
            }

            if (cuisineExpanded) {
                if (isLoadingAreas) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        LoadingIndicator()
                    }
                } else {
                    items(areas) { area ->
                        BrowseItem(
                            text = area.strArea,
                            onClick = { onAreaClick(area.strArea) }
                        )
                    }
                }
            }

            // Letter Section Header
            item(span = { GridItemSpan(maxLineSpan) }) {
                Spacer(modifier = Modifier.height(8.dp))
                CollapsibleHeader(
                    title = "By First Letter",
                    isExpanded = lettersExpanded,
                    onToggle = { lettersExpanded = !lettersExpanded }
                )
            }

            if (lettersExpanded) {
                items(alphabet) { letter ->
                    BrowseItem(
                        text = letter,
                        onClick = { onLetterClick(letter) }
                    )
                }
            }

            // Bottom spacing
            item(span = { GridItemSpan(maxLineSpan) }) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun CollapsibleHeader(title: String, isExpanded: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 18.sp
        )
        Icon(
            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            contentDescription = if (isExpanded) "Collapse" else "Expand",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun ErrorText(message: String) {
    Text(
        text = message,
        color = MaterialTheme.colorScheme.error,
        modifier = Modifier.padding(vertical = 16.dp)
    )
}

@Composable
fun BrowseItem(text: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
