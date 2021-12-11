package com.example.foodapplication.data.databse

import androidx.room.TypeConverter
import com.example.foodapplication.modals.FoodRecipes
import com.example.foodapplication.modals.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipesTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun foodRecipeToString(foodRecipes: FoodRecipes):String {
        return gson.toJson(foodRecipes)
    }

    @TypeConverter
    fun stringToFoodRecipe(data:String):FoodRecipes{
        val listType = object: TypeToken<FoodRecipes>() {}.type
        return gson.fromJson(data,listType)
    }

    @TypeConverter
    fun resultToString(result:Result):String{
        return gson.toJson(result)
    }

    @TypeConverter
    fun stringToResult(data:String):Result{
        val listType = object: TypeToken<Result>() {}.type
        return gson.fromJson(data,listType)
    }
}