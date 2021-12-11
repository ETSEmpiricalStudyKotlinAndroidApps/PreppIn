package com.example.foodapplication.util

import androidx.recyclerview.widget.DiffUtil


class RecipesDiffUtil<T>   (
        private val oldList : List<T>,
        private val newList:  List<T>

        ): DiffUtil.Callback(){

    // basically return the size of old list
    override fun getOldListSize(): Int {
       return oldList.size
    }

    override fun getNewListSize(): Int {
       return newList.size
    }

    // Called by the Diff Util to Decide whether two Object represent the same Item in the old and Mew List
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       return  oldList[oldItemPosition] === newList[newItemPosition]
    }
//Checks weather the two items hve the same data , You can change its Behavior depending on Your UI
    // This method is called By only DiffUtil only if areItemsTheSame returns true
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       return oldList[oldItemPosition] == newList[newItemPosition]
    }

}



