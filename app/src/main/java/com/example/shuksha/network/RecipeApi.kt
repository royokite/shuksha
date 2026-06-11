package com.example.shuksha.network


import com.example.shuksha.data.MealResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeApi {

    @GET("search.php")
    suspend fun searchMeals(
        @Query("s") query: String
    ): MealResponse
}