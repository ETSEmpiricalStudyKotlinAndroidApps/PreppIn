package com.example.foodapplication.bindingAdapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.example.foodapplication.R
import com.example.foodapplication.modals.Result
import com.example.foodapplication.ui.fragments.recipes.RecipiesFragmentDirections
import org.jsoup.Jsoup


class RecipesRowBinding {

    companion object {

        @BindingAdapter("onRecipeClickListener")
        @JvmStatic
        fun onRecipeClickListener(recipeRowLayout : ConstraintLayout, result:Result){
            Log.d("onRecipeClickListener","called")
            recipeRowLayout.setOnClickListener {
                try {
                    val action = RecipiesFragmentDirections.actionRecipesFragmentToDetailsActivity(result )
                  recipeRowLayout.findNavController().navigate(action)
                } catch (e:Exception){
                    Log.d("onRecipeClickListener",e.toString())
                }
            }
        }

        @BindingAdapter("image")
        @JvmStatic
        fun image(imageView: ImageView, imageUrl: String) {
            imageView.load(imageUrl) {
                crossfade(600)
            }
        }




        @BindingAdapter("setNumberOfLikes")
        @JvmStatic
        fun setNumberOfLikes(textView: TextView,likes:Int){
            textView.text = likes.toString()
        }
        @BindingAdapter("setNumberOfMin")
        @JvmStatic
        fun setNumberOfMin(textView: TextView, Min:Int){
            textView.text = Min.toString()
        }

         @BindingAdapter("applyVeganColor")
         @JvmStatic
        fun applyVeganColor(view:View,vegan:Boolean){
            if (vegan){
                when(view){
                    is TextView ->{
                        view.setTextColor(
                                ContextCompat.getColor(
                                        view.context, R.color.green
                                )
                        )
                    }
                    is ImageView ->{
                        view.setColorFilter(
                                ContextCompat.getColor(
                                        view.context, R.color.green
                                )
                        )
                    }
                }
            }
        }

        @BindingAdapter("parseHtml")
        @JvmStatic
        fun parseHtml(textView: TextView,description: String?){
            if(description!= null){
                val desc = Jsoup.parse(description).text()
                textView.text = desc
            }
        }
    }

}