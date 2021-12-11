package com.example.foodapplication.modals


import com.google.gson.annotations.SerializedName

data class FoodTrivia(
    @SerializedName("text")
    val text: String
)