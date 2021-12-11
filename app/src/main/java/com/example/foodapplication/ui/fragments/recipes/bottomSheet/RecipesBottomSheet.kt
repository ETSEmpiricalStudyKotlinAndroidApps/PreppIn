package com.example.foodapplication.ui.fragments.recipes.bottomSheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.foodapplication.R
import com.example.foodapplication.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.foodapplication.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.foodapplication.viewmodals.RecipesViewModal
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.recipes_bottom_sheet.view.*
import java.lang.Exception
import java.util.*


class RecipesBottomSheet : BottomSheetDialogFragment() {

    private lateinit var recipesViewModal: RecipesViewModal

    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipesViewModal = ViewModelProvider(requireActivity()).get(RecipesViewModal::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val mview = inflater.inflate(R.layout.recipes_bottom_sheet, container, false)

        recipesViewModal.readMealAndDietType.asLiveData().observe(viewLifecycleOwner,{value ->
            mealTypeChip = value.selectedMealType
            dietTypeChip = value.selectedDietType
            updateChip(value.selectedMealTypeId,mview.mealType_chipGroup)
            updateChip(value.selectedDietTypeId,mview.dietType_chipGroup)
        })
        mview.mealType_chipGroup.setOnCheckedChangeListener { group, SelectedChipId ->
            val chip = group.findViewById<Chip>(SelectedChipId)
            val selectedMealType = chip.text.toString().toLowerCase(Locale.ROOT)
            mealTypeChip = selectedMealType
            mealTypeChipId = SelectedChipId
        }

        mview.dietType_chipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedDietType = chip.text.toString().toLowerCase(Locale.ROOT)
            dietTypeChip = selectedDietType
            dietTypeChipId = selectedChipId
        }

        mview.apply_button.setOnClickListener {
            recipesViewModal.saveMealAndDietType(mealTypeChip,mealTypeChipId,dietTypeChip,dietTypeChipId)

            val action = RecipesBottomSheetDirections.actionRecipesBottomSheetToRecipesFragment(true)
            findNavController().navigate(action)
        }

        return mview
    }

    private fun updateChip(chipId: Int, chipgroup: ChipGroup?) {

        if(chipId!=0){
            try {
                chipgroup?.findViewById<Chip>(chipId)?.isChecked = true
            } catch(e: Exception){
                Log.d("RecipesBottomSheet",e.message.toString())
            }
        }
    }

}