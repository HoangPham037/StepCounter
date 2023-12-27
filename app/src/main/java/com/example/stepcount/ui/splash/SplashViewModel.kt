package com.example.stepcount.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel: ViewModel() {
    private val _navigateToNextScreen = MutableLiveData<Unit>()
    val navigateToNextScreen : LiveData<Unit> get() = _navigateToNextScreen

    fun startDelay() {
        viewModelScope.launch {
            delay(3000)
            _navigateToNextScreen.value = Unit
        }
    }
}