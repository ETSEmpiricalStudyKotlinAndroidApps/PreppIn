package com.example.foodapplication.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapplication.viewmodals.MainViewModel
import com.example.foodapplication.R
import com.example.foodapplication.util.NetworkResult
import com.example.foodapplication.adapters.RecipesAdapter
import com.example.foodapplication.databinding.FragmentRecipiesBinding
import com.example.foodapplication.util.NetworkListener
import com.example.foodapplication.util.observeOnce
import com.example.foodapplication.viewmodals.RecipesViewModal
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RecipiesFragment : Fragment(),SearchView.OnQueryTextListener {

    private val args by navArgs<RecipiesFragmentArgs>()


    private var _binding :FragmentRecipiesBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModal: RecipesViewModal
    private val mAdapter by lazy { RecipesAdapter() }
    private lateinit var mview:View



    private lateinit var networkListener: NetworkListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipesViewModal = ViewModelProvider(requireActivity()).get(RecipesViewModal::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=  FragmentRecipiesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel



        setUpRecyclerView()
      //  readDataBase()
        setHasOptionsMenu(true)
        recipesViewModal.readBackOnline.observe(viewLifecycleOwner, {
            recipesViewModal.backOnline = it
        })

        lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    Log.d("NetworkListener", status.toString())
                   recipesViewModal.networkStatus = status
                   recipesViewModal.showNetworkStatus()
                    readDataBase()
                }
        }

        binding.recipesFab.setOnClickListener {
            if (recipesViewModal.networkStatus){
               findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
            } else{
                recipesViewModal.showNetworkStatus()
            }
        }

        return binding.root
    }


    private fun setUpRecyclerView(){
        binding.recyclerView.adapter = mAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEfect()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipes_menu,menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null){
            searchApiData(query)
        }
       return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    private fun loadDataFromCache(){

        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner,{Database->
                if(Database.isNotEmpty()){
                    mAdapter.setData(Database[0].foodRecipes)
                }
            })
        }
    }

    private fun readDataBase() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner,{database ->
                if (database.isNotEmpty() && !args.backFromBottomSheet){
                    Log.d("RecipesFragment", "readDataBase Called!")
                    mAdapter.setData(database[0].foodRecipes)
                    hideShimmerEffect()

                }else{
                    requestApiData()
                }
            })
        }
    }

    private fun requestApiData(){
        Log.d("RecipesFragment", "requestApiData Called!")
        mainViewModel.getRecipes(recipesViewModal.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner, { response ->
            when(response){
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let {
                        mAdapter.setData(it)
                    }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                            requireContext(),response.message.toString(),Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading ->{
                    showShimmerEfect()
                }

            }

        })
    }

    private fun searchApiData(searchQuery:String){
        showShimmerEfect()
        mainViewModel.searchRecipes(recipesViewModal.applySearchQuery(searchQuery))
        mainViewModel.searchedRecipesRespose.observe(viewLifecycleOwner,{response->
            when(response){
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    val foodRecipe = response.data
                    foodRecipe?.let {
                        mAdapter.setData(it)
                    }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),response.message.toString(),Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading ->{
                    showShimmerEfect()
                }
            }

        })
    }

    private fun showShimmerEfect(){

       binding.recyclerView.showShimmer()
    }

    private fun hideShimmerEffect(){
        binding.recyclerView.hideShimmer()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}