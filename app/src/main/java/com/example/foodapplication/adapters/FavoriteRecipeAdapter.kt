package com.example.foodapplication.adapters

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapplication.R
import com.example.foodapplication.data.databse.entities.FavoritesEntity
import com.example.foodapplication.databinding.FavoriteRecipeRowLayoutBinding
import com.example.foodapplication.ui.favouriteRecipiesFragmentDirections
import com.example.foodapplication.util.RecipesDiffUtil
import com.example.foodapplication.viewmodals.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.favorite_recipe_row_layout.view.*

class FavoriteRecipeAdapter(private val requireActivity: FragmentActivity,
private var mainViewModel: MainViewModel) :
    RecyclerView.Adapter<FavoriteRecipeAdapter.MyViewHolder>(), ActionMode.Callback {

    private var multiSelection = false
    private lateinit var mActionMode: ActionMode
    private lateinit var rootview:View

    private var myviewHolders = arrayListOf<MyViewHolder>()
    private var selectedRecipes = arrayListOf<FavoritesEntity>()
    private var favoriteRecipes = emptyList<FavoritesEntity>()

    class MyViewHolder(private val binding: FavoriteRecipeRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favoritesEntity: FavoritesEntity) {
            binding.favoritesEntity = favoritesEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoriteRecipeRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        myviewHolders.add(holder)

        rootview  = holder.itemView.rootView
        val currentRecipe = favoriteRecipes[position]
        holder.bind(currentRecipe)


        /**
         *Single Click Listener
         **/
        holder.itemView.favorite_recipesRowLayout.setOnClickListener {

            if (multiSelection) {
                applySelection(holder, currentRecipe)
            } else {
                val action =
                    favouriteRecipiesFragmentDirections.actionFavouriteRecipiesFragmentToDetailsActivity(
                        currentRecipe.result
                    )
                holder.itemView.findNavController().navigate(action)
            }
        }
        /**
         *Long Click Listener
         **/
        holder.itemView.favorite_recipesRowLayout.setOnLongClickListener {

            if (!multiSelection) {
                multiSelection = true
                requireActivity.startActionMode(this)
                true
            } else {
                multiSelection = false
                false
            }

        }
    }

    private fun applySelection(holder: MyViewHolder, currentRecipe: FavoritesEntity) {
        if (selectedRecipes.contains(currentRecipe)) {
            selectedRecipes.remove(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
            applyActionModeTitle()
        } else {
            selectedRecipes.add(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundLightColor, R.color.colorPrimary)
            applyActionModeTitle()
        }
    }

    private fun changeRecipeStyle(holder: MyViewHolder, backgroundColor: Int, strokeColor: Int) {

        holder.itemView.favorite_recipesRowLayout.setBackgroundColor(
            ContextCompat.getColor(requireActivity, backgroundColor)
        )

        holder.itemView.favorite_row_cardView.strokeColor =
            ContextCompat.getColor(requireActivity, strokeColor)
    }


    private fun applyActionModeTitle(){
        when(selectedRecipes.size){
            0->{
                mActionMode.finish()
            }
            1->{
                mActionMode.title = "${selectedRecipes.size} item selected"
            }
            else ->{
                mActionMode.title = "${selectedRecipes.size} items selected"
            }

        }
    }

    override fun getItemCount(): Int {
        return favoriteRecipes.size
    }


    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.favorites_contextual_menu, menu)
        mActionMode = mode!!
        applyStatusBarColumn(R.color.ContextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, menu: MenuItem?): Boolean {
        if(menu?.itemId == R.id.delete_favorite_recipe_menu ){
            selectedRecipes.forEach {
                mainViewModel.deleteFavoriteRecipe(it)
                showSnackBar("${selectedRecipes.size} Recipe/s removed.")
                multiSelection = false
                selectedRecipes.clear()
                mode?.finish()
            }
        }
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {

        myviewHolders.forEach { holder->

            changeRecipeStyle(holder,R.color.cardBackgroundColor,R.color.strokeColor)

        }

        multiSelection = false
        selectedRecipes.clear()
        applyStatusBarColumn(R.color.statusBarColor)
    }

    private fun applyStatusBarColumn(color: Int) {
        requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity, color)
    }

    fun setData(newFavoriteRecipes: List<FavoritesEntity>) {
        val favoriteRecipesDiffUtil =
            RecipesDiffUtil(favoriteRecipes, newFavoriteRecipes)

        val diffUtilResult = DiffUtil.calculateDiff(favoriteRecipesDiffUtil)

        favoriteRecipes = newFavoriteRecipes
        diffUtilResult.dispatchUpdatesTo(this)
    }
    private fun showSnackBar(message:String){
        Snackbar.make(rootview,message,Snackbar.LENGTH_SHORT).setAction("Okay"){}.show()
    }

    fun clearContextualActionMode(){
        if(this::mActionMode.isInitialized){
            mActionMode.finish()
        }
    }

}
