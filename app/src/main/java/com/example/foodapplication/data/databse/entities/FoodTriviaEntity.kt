package com.example.foodapplication.data.databse.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodapplication.modals.FoodTrivia
import com.example.foodapplication.util.Constants.Companion.FOOD_TRIVIA_TABLE


@Entity(tableName = FOOD_TRIVIA_TABLE)
class FoodTriviaEntity(
    @Embedded
   var foodTrivia: FoodTrivia
) {
    @PrimaryKey(autoGenerate = false)
    var id : Int = 0
}