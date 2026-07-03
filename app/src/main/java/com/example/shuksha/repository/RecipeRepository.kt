package com.example.shuksha.repository

import com.example.shuksha.data.Meal
import com.example.shuksha.data.AreaItem
import com.example.shuksha.data.CategoryItem
import com.example.shuksha.network.RetrofitInstance

class RecipeRepository {

    suspend fun searchRecipes(query: String): List<Meal> {
        return RetrofitInstance.api.searchMeals(query).meals ?: emptyList()
    }

    suspend fun getMealDetails(mealId: String): List<Meal> {
        return RetrofitInstance.api.getMealDetails(mealId).meals ?: emptyList()
    }

    suspend fun getRandomMeal(): List<Meal> {
        return RetrofitInstance.api.getRandomMeal().meals ?: emptyList()
    }

    suspend fun getMealsByArea(area: String): List<Meal> {
        return RetrofitInstance.api.getMealsByArea(area).meals ?: emptyList()
    }

    suspend fun getMealsByCategory(category: String): List<Meal> {
        return RetrofitInstance.api.getMealsByCategory(category).meals ?: emptyList()
    }

    suspend fun getMealsByFirstLetter(letter: String): List<Meal> {
        return RetrofitInstance.api.getMealsByFirstLetter(letter).meals ?: emptyList()
    }

    suspend fun getAreaList(): List<AreaItem> {
        return RetrofitInstance.api.getAreaList().meals ?: emptyList()
    }

    suspend fun getCategoryList(): List<CategoryItem> {
        return RetrofitInstance.api.getCategoryList().meals ?: emptyList()
    }
}
