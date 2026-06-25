package com.example.shuksha.data

import com.google.gson.annotations.SerializedName

data class AreaListResponse(
    @SerializedName("meals")
    val meals: List<AreaItem>? = null
)

data class AreaItem(
    @SerializedName("strArea")
    val strArea: String
)

data class CategoryListResponse(
    @SerializedName("meals")
    val meals: List<CategoryItem>? = null
)

data class CategoryItem(
    @SerializedName("strCategory")
    val strCategory: String
)
