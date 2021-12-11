package com.example.foodapplication.data.databse

import androidx.room.*
import com.example.foodapplication.data.databse.entities.FavoritesEntity
import com.example.foodapplication.data.databse.entities.FoodTriviaEntity
import com.example.foodapplication.data.databse.entities.RecipesEntity
import com.example.foodapplication.modals.FoodTrivia
import kotlinx.coroutines.flow.Flow


@Dao
interface RecipesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertRecipes(recipesEntity: RecipesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertFavoriteRecipes(favoritesEntity: FavoritesEntity)

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertFoodTrivia(foodTriviaEntity: FoodTriviaEntity)

   @Query("SELECT * FROM recipes_table ORDER BY id ASC")
   fun readRecipes(): Flow<List<RecipesEntity>>

    @Query("SELECT * FROM favorite_recipes_table ORDER BY id ASC")
    fun readFavoriteRecipes(): Flow<List<FavoritesEntity>>

    @Query("SELECT * FROM food_Trivia_table ORDER BY id ASC")
    fun readFoodTrivia():Flow<List<FoodTriviaEntity>>

    @Delete
    suspend fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity)

    @Query("DELETE FROM favorite_recipes_table")
    suspend fun deleteAllFavoriteRecipe()


}