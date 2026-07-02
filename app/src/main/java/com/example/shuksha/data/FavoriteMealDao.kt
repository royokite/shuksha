package com.example.shuksha.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMealDao {
    @Query("SELECT * FROM favorite_meals")
    fun getAllFavorites(): Flow<List<FavoriteMeal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(meal: FavoriteMeal)

    @Delete
    suspend fun deleteFavorite(meal: FavoriteMeal)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_meals WHERE idMeal = :id)")
    suspend fun isFavorite(id: String): Boolean
}
