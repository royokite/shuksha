package com.example.shuksha.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_recipes")
data class CustomRecipe(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String,
    val instructions: String,
    val ingredients: String, // Stored as a comma-separated or JSON string
    val imageUrl: String? = null
)
