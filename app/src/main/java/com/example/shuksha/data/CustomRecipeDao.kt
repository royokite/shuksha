package com.example.shuksha.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomRecipeDao {
    @Query("SELECT * FROM custom_recipes")
    fun getAllCustomRecipes(): Flow<List<CustomRecipe>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: CustomRecipe)

    @Delete
    suspend fun deleteRecipe(recipe: CustomRecipe)
}
