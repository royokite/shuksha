package com.example.shuksha.repository

import com.example.shuksha.data.Meal
import com.example.shuksha.network.RetrofitInstance

class RecipeRepository {

    suspend fun searchRecipes(query: String): List<Meal> {
        return RetrofitInstance.api.searchMeals(query).meals
    }
}