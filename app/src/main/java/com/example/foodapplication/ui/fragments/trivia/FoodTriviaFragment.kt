package com.example.foodapplication.ui.fragments.trivia

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.foodapplication.R
import com.example.foodapplication.databinding.FragmentFoodTriviaBinding
import com.example.foodapplication.util.Constants.Companion.API_KEY
import com.example.foodapplication.util.NetworkResult
import com.example.foodapplication.viewmodals.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FoodTriviaFragment : Fragment() {

    private val mainViewModel by viewModels<MainViewModel>()
    private var _binding: FragmentFoodTriviaBinding? = null
    private val binding get() = _binding!!

    private var foodTrivia = "No Food Trivia"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFoodTriviaBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = mainViewModel

        setHasOptionsMenu(true)
        mainViewModel.getFoodTrivia(API_KEY)
        mainViewModel.foodTriviaResponse.observe(viewLifecycleOwner, { response ->

            when (response) {
                is NetworkResult.Success -> {
                    binding.foodTriviaTextView.text = response.data?.text
                    if(response.data !=null){
                        foodTrivia = response.data.text
                    }
                }
                is NetworkResult.Error -> {

                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {

                    Log.d("FoodTriviaFragment", "Loading")
                }

            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
      inflater.inflate(R.menu.food_trivia_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       if(item.itemId == R.id.share_food_trivia){
           val shareIntent = Intent().apply {
               this.action = Intent.ACTION_SEND
               this.putExtra(Intent.EXTRA_TEXT,foodTrivia)
               this.type = "text/plain"
           }
           startActivity(shareIntent)
       }
        return super.onOptionsItemSelected(item)
    }

    private fun loadDataFromCache(){
       lifecycleScope.launch {
           mainViewModel.readFoodTrivia.observe(viewLifecycleOwner,{ database ->
               if(!database.isNullOrEmpty()){
                   binding.foodTriviaTextView.text = database[0].foodTrivia.text
                   foodTrivia = database[0].foodTrivia.text
               }

           })
       }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}