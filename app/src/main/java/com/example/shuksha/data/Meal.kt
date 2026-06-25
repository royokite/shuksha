package com.example.shuksha.data

import com.google.gson.annotations.SerializedName

data class Meal(
    @SerializedName("idMeal")
    val idMeal: String,
    @SerializedName("strMeal")
    val strMeal: String,
    @SerializedName("strMealThumb")
    val strMealThumb: String? = null,
    @SerializedName("strCategory")
    val strCategory: String? = null,
    @SerializedName("strArea")
    val strArea: String? = null,
    @SerializedName("strInstructions")
    val strInstructions: String? = null,
    @SerializedName("strIngredient1")
    val strIngredient1: String? = null,
    @SerializedName("strIngredient2")
    val strIngredient2: String? = null,
    @SerializedName("strIngredient3")
    val strIngredient3: String? = null,
    @SerializedName("strIngredient4")
    val strIngredient4: String? = null,
    @SerializedName("strIngredient5")
    val strIngredient5: String? = null,
    @SerializedName("strIngredient6")
    val strIngredient6: String? = null,
    @SerializedName("strIngredient7")
    val strIngredient7: String? = null,
    @SerializedName("strIngredient8")
    val strIngredient8: String? = null,
    @SerializedName("strIngredient9")
    val strIngredient9: String? = null,
    @SerializedName("strIngredient10")
    val strIngredient10: String? = null,
    @SerializedName("strMeasure1")
    val strMeasure1: String? = null,
    @SerializedName("strMeasure2")
    val strMeasure2: String? = null,
    @SerializedName("strMeasure3")
    val strMeasure3: String? = null,
    @SerializedName("strMeasure4")
    val strMeasure4: String? = null,
    @SerializedName("strMeasure5")
    val strMeasure5: String? = null,
    @SerializedName("strMeasure6")
    val strMeasure6: String? = null,
    @SerializedName("strMeasure7")
    val strMeasure7: String? = null,
    @SerializedName("strMeasure8")
    val strMeasure8: String? = null,
    @SerializedName("strMeasure9")
    val strMeasure9: String? = null,
    @SerializedName("strMeasure10")
    val strMeasure10: String? = null
) {
    fun getIngredientsList(): List<Pair<String, String>> {
        return listOfNotNull(
            if (!strIngredient1.isNullOrEmpty()) strIngredient1 to (strMeasure1 ?: "") else null,
            if (!strIngredient2.isNullOrEmpty()) strIngredient2 to (strMeasure2 ?: "") else null,
            if (!strIngredient3.isNullOrEmpty()) strIngredient3 to (strMeasure3 ?: "") else null,
            if (!strIngredient4.isNullOrEmpty()) strIngredient4 to (strMeasure4 ?: "") else null,
            if (!strIngredient5.isNullOrEmpty()) strIngredient5 to (strMeasure5 ?: "") else null,
            if (!strIngredient6.isNullOrEmpty()) strIngredient6 to (strMeasure6 ?: "") else null,
            if (!strIngredient7.isNullOrEmpty()) strIngredient7 to (strMeasure7 ?: "") else null,
            if (!strIngredient8.isNullOrEmpty()) strIngredient8 to (strMeasure8 ?: "") else null,
            if (!strIngredient9.isNullOrEmpty()) strIngredient9 to (strMeasure9 ?: "") else null,
            if (!strIngredient10.isNullOrEmpty()) strIngredient10 to (strMeasure10 ?: "") else null
        )
    }
}