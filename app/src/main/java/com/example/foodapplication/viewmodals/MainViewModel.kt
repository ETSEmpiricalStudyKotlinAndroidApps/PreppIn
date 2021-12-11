package com.example.foodapplication.viewmodals

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.foodapplication.data.Repository
import com.example.foodapplication.data.databse.entities.FavoritesEntity
import com.example.foodapplication.data.databse.entities.FoodTriviaEntity
import com.example.foodapplication.data.databse.entities.RecipesEntity
import com.example.foodapplication.modals.FoodRecipes
import com.example.foodapplication.modals.FoodTrivia
import com.example.foodapplication.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception


class MainViewModel @ViewModelInject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {


    /**ROOM DATABASE*/

    val readFoodTrivia:LiveData<List<FoodTriviaEntity>> = repository.local.readFoodTrivia().asLiveData()
    val readRecipes: LiveData<List<RecipesEntity>> = repository.local.readRecipes().asLiveData()
    val readFavoritesRecipes: LiveData<List<FavoritesEntity>> =
        repository.local.readFavoriteRecipes().asLiveData()

    private fun insertRecipes(recipesEntity: RecipesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipes(recipesEntity)
        }

    fun insertFavoriteRecipe(favoritesEntity: FavoritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavoriteRecipes(favoritesEntity)
        }

    fun insertFoodTrivia(foodTriviaEntity: FoodTriviaEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFoodTrivia(foodTriviaEntity)
        }

    fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavoriteRecipe(favoritesEntity)
        }

    fun deleteAllFavoriteRecipe() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteAllFavoritesRecipes()
        }

    /**RETROFIT*/
    var recipesResponse: MutableLiveData<NetworkResult<FoodRecipes>> = MutableLiveData()
    var searchedRecipesRespose: MutableLiveData<NetworkResult<FoodRecipes>> = MutableLiveData()
    var foodTriviaResponse: MutableLiveData<NetworkResult<FoodTrivia>> = MutableLiveData()

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    fun searchRecipes(searchQuery: Map<String, String>) = viewModelScope.launch {
        searchRecipesSafeCall(searchQuery)
    }

    fun getFoodTrivia(apiKey: String) = viewModelScope.launch {
        getFoodJokeSafeCall(apiKey)
    }


    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {

                val response = repository.remote.getRecipes(queries)
                recipesResponse.value = handleFoodRecipesResponse(response)

                val foodRecipes = recipesResponse.value!!.data
                if (foodRecipes != null) {
                    offlineCacheRecipes(foodRecipes)
                }

            } catch (e: Exception) {
                recipesResponse.value = NetworkResult.Error("Recipes not found.")
            }
        } else {
            recipesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }


    private suspend fun searchRecipesSafeCall(searchQuery: Map<String, String>) {
        searchedRecipesRespose.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {

                val response = repository.remote.searchRecipes(searchQuery)
                searchedRecipesRespose.value = handleFoodRecipesResponse(response)

            } catch (e: Exception) {
                searchedRecipesRespose.value = NetworkResult.Error("Recipes not found.")
            }
        } else {
            searchedRecipesRespose.value = NetworkResult.Error("No Internet Connection.")
        }

    }

    private suspend fun getFoodJokeSafeCall(apiKey: String) {

        foodTriviaResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {

                val response = repository.remote.getFoodTrivia(apiKey)
                foodTriviaResponse.value = handleFoodTriviaResponse(response)

                val foodTrivia = foodTriviaResponse.value!!.data
                if(foodTrivia!= null){
                    offlineCacheFoodTrivia(foodTrivia)
                }
            } catch (e: Exception) {
                foodTriviaResponse.value = NetworkResult.Error("Recipes not found.")
            }
        } else {
            foodTriviaResponse.value = NetworkResult.Error("No Internet Connection.")
        }

    }

    private fun offlineCacheRecipes(foodRecipes: FoodRecipes) {

        val recipesEntity = RecipesEntity(foodRecipes)
        insertRecipes(recipesEntity)
    }

    private fun offlineCacheFoodTrivia(foodTrivia: FoodTrivia) {

        val foodTriviaEntity = FoodTriviaEntity(foodTrivia)
       insertFoodTrivia(foodTriviaEntity)
    }

    private fun handleFoodRecipesResponse(response: Response<FoodRecipes>): NetworkResult<FoodRecipes>? {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                NetworkResult.Error("API Key Limited.")
            }
            response.body()!!.results.isNullOrEmpty() -> {
                NetworkResult.Error("Recipes not found.")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                NetworkResult.Success(foodRecipes!!)
            }
            else -> {
                NetworkResult.Error(response.message())
            }
        }
    }

    private fun handleFoodTriviaResponse(response: Response<FoodTrivia>): NetworkResult<FoodTrivia>? {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited.")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}