package com.example.foodapplication.data.Network

import com.example.foodapplication.modals.FoodRecipes
import com.example.foodapplication.modals.FoodTrivia
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface FoodRecipesApi {

    @GET("/recipes/complexSearch")
    suspend fun getRecipes(
        @QueryMap queries: Map<String, String>
    ): Response<FoodRecipes>


    @GET("/recipes/complexSearch")
    suspend fun searchRecipes(
        @QueryMap searchQuery: Map<String, String>
    ): Response<FoodRecipes>

    @GET("food/trivia/random")
    suspend fun getFoodTrivia(
        @Query("apiKey") apiKey : String
    ):Response<FoodTrivia>
}