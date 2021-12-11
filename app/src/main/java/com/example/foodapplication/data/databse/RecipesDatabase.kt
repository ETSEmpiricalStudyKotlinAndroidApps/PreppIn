package com.example.foodapplication.data.databse

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.foodapplication.data.databse.entities.FavoritesEntity
import com.example.foodapplication.data.databse.entities.FoodTriviaEntity
import com.example.foodapplication.data.databse.entities.RecipesEntity

@Database(
    entities = [RecipesEntity::class,FavoritesEntity::class,FoodTriviaEntity::class],
           version = 1,
    exportSchema = false
    )
@TypeConverters(RecipesTypeConverter::class)
abstract  class RecipesDatabase : RoomDatabase() {
    abstract fun recipesDao(): RecipesDao

}