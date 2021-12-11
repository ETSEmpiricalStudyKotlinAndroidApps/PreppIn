package com.example.foodapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.foodapplication.R
import com.example.foodapplication.modals.ExtendedIngredient
import com.example.foodapplication.util.Constants.Companion.BASE_IMAGE_URL
import com.example.foodapplication.util.RecipesDiffUtil
import kotlinx.android.synthetic.main.ingredients_row_layout.view.*

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>() {

    private var ingredientsList = emptyList<ExtendedIngredient>()
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.ingredients_row_layout,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.ingredient_imageView.load(BASE_IMAGE_URL + ingredientsList[position].image){
            crossfade(600)
        }
        holder.itemView.ingredient_name.text = ingredientsList[position].name.capitalize()
        holder.itemView.ingredient_amount.text = ingredientsList[position].amount.toString()
        holder.itemView.ingredient_unit.text=ingredientsList[position].unit
        holder.itemView.ingredient_Consistency.text=ingredientsList[position].consistency
        holder.itemView.ingredient_original.text=ingredientsList[position].original
    }

    override fun getItemCount(): Int {
      return ingredientsList.size
    }

    fun setData(ingredient: List<ExtendedIngredient>){
        val recipesDiffUtil = RecipesDiffUtil(ingredientsList,ingredient)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        ingredientsList = ingredient
        diffUtilResult.dispatchUpdatesTo(this)
    }
}
