package com.example.shuksha.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class NavItem(
    val label: String,
    val icon: ImageVector
) {
    HOME("Home", Icons.Default.Home),
    EXPLORE("Explore", Icons.Default.Favorite),
    SEARCH("Search", Icons.Default.Search),
    BROWSE("Browse", Icons.Default.Settings)
}
