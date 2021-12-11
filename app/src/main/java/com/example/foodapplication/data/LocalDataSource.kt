package com.example.foodapplication.data


import com.example.foodapplication.data.databse.RecipesDao
import com.example.foodapplication.data.databse.entities.FavoritesEntity
import com.example.foodapplication.data.databse.entities.FoodTriviaEntity
import com.example.foodapplication.data.databse.entities.RecipesEntity
import com.example.foodapplication.modals.FoodTrivia
import kotlinx.coroutines.flow.Flow


import javax.inject.Inject

class LocalDataSource @Inject constructor(
        private val recipesDao: RecipesDao
) {

     fun readRecipes(): Flow<List<RecipesEntity>> {
        return recipesDao.readRecipes()
    }
    fun readFavoriteRecipes(): Flow<List<FavoritesEntity>> {
        return recipesDao.readFavoriteRecipes()
    }
     fun readFoodTrivia():Flow<List<FoodTriviaEntity>>{
        return recipesDao.readFoodTrivia()
    }

    suspend fun insertRecipes(recipesEntity: RecipesEntity){
        recipesDao.insertRecipes(recipesEntity)
    }

    suspend fun insertFavoriteRecipes(favoritesEntity: FavoritesEntity){
        recipesDao.insertFavoriteRecipes(favoritesEntity)
    }

    suspend fun insertFoodTrivia(foodTriviaEntity: FoodTriviaEntity){
        recipesDao.insertFoodTrivia(foodTriviaEntity)
    }

    suspend fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity){
        recipesDao.deleteFavoriteRecipe(favoritesEntity)
    }
    suspend fun deleteAllFavoritesRecipes(){
        recipesDao.deleteAllFavoriteRecipe()
    }
}