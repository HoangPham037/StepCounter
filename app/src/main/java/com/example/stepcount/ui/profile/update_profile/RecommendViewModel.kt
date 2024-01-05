package com.example.stepcount.ui.profile.update_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecommendViewModel : ViewModel() {

    private val _gender = MutableLiveData("Male")
    val gender: LiveData<String> get() = _gender

    fun setGender(gender: String) {
        _gender.postValue(gender)
    }

    private val _height = MutableLiveData<Int>(160)
    val height: LiveData<Int> get() = _height
    fun setHeight(height: Int) {
        _height.postValue(height)
    }

    private val _weightKg = MutableLiveData<String>("55")
    val weightKg: LiveData<String> get() = _weightKg

    fun setWeightKg(weightKg: String) {
        _weightKg.postValue(weightKg)
    }

    private val _weightG = MutableLiveData<String>("0")
    val weightG: LiveData<String> get() = _weightG
    fun setWeightG(weightG: String) {
        _weightG.postValue(weightG)
    }

    private val _totalWeight = MediatorLiveData<String>()
    val totalWeight: LiveData<String> get() = _totalWeight

    init {
        _totalWeight.addSource(weightKg) { updateTotalWeight() }
        _totalWeight.addSource(weightG) { updateTotalWeight() }
    }

    private fun updateTotalWeight() {
        val kg = _weightKg.value ?: "55"
        val g = _weightG.value ?: "0"
        _totalWeight.value = "$kg.$g"
    }

    private val _age = MutableLiveData<Int>(18)
    val age: LiveData<Int> get() = _age

    fun setAge(age: Int) {
        _age.postValue(age)
    }
}