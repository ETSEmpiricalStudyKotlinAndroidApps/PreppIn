package com.example.foodapplication.data.databse.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodapplication.modals.Result
import com.example.foodapplication.util.Constants.Companion.FAVORITE_RECIPES_TABLE

@Entity(tableName = FAVORITE_RECIPES_TABLE)
class FavoritesEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: Result
) {

}