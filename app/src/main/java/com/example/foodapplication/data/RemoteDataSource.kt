package com.example.foodapplication.data

import com.example.foodapplication.data.Network.FoodRecipesApi
import com.example.foodapplication.modals.FoodRecipes
import com.example.foodapplication.modals.FoodTrivia
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
        private val foodRecipesApi: FoodRecipesApi
){
    suspend fun getRecipes(queries:Map<String,String>):Response<FoodRecipes>{
        return foodRecipesApi.getRecipes(queries)
    }

    suspend fun searchRecipes(searchQuery: Map<String,String>):Response<FoodRecipes>{
        return foodRecipesApi.searchRecipes(searchQuery)
    }

    suspend fun getFoodTrivia(apiKey:String): Response<FoodTrivia>{
        return foodRecipesApi.getFoodTrivia(apiKey)
    }
}