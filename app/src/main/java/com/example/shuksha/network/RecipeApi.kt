package com.example.shuksha.network


import com.example.shuksha.data.AreaListResponse
import com.example.shuksha.data.CategoryListResponse
import com.example.shuksha.data.MealResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeApi {

    @GET("search.php")
    suspend fun searchMeals(
        @Query("s") query: String
    ): MealResponse

    @GET("lookup.php")
    suspend fun getMealDetails(@Query("i") mealId: String): MealResponse

    @GET("random.php")
    suspend fun getRandomMeal(): MealResponse

    @GET("filter.php")
    suspend fun getMealsByArea(@Query("a") area: String): MealResponse

    @GET("filter.php")
    suspend fun getMealsByCategory(@Query("c") category: String): MealResponse

    @GET("search.php")
    suspend fun getMealsByFirstLetter(@Query("f") firstLetter: String): MealResponse

    @GET("list.php")
    suspend fun getAreaList(@Query("a") list: String = "list"): AreaListResponse

    @GET("list.php")
    suspend fun getCategoryList(@Query("c") list: String = "list"): CategoryListResponse
}