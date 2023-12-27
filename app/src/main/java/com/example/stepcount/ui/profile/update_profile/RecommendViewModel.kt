package com.example.stepcount.ui.profile.update_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stepcount.ui.profile.update_profile.RecommendProfileFragment.Companion.STEP_RANGE

class RecommendViewModel : ViewModel() {
    private val _selected = MutableLiveData(false)
    val selected : LiveData<Boolean> = _selected

    fun setSelected(isSelect:Boolean){
        _selected.value = isSelect
    }

    val optionsList = MutableLiveData<ArrayList<String>>()
    val selectedOption = MutableLiveData<String>()

    fun initializeOptionsList(){
        optionsList.value = ArrayList(STEP_RANGE.map { it.toString() })
    }

    fun onOptionSelected(option: String) {
        selectedOption.value = option
    }

    fun setInitialSelectedOption(position: Int) {
        val options = optionsList.value
        if (options != null && position >= 0 && position < options.size) {
            selectedOption.value = options[position]
        }
    }


}