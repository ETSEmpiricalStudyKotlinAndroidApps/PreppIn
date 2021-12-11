package com.example.foodapplication.data.databse.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodapplication.modals.FoodRecipes
import com.example.foodapplication.util.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(var foodRecipes: FoodRecipes) {

    @PrimaryKey(autoGenerate = false)
    var id:Int =0
}