package com.example.stepcount

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShareDataViewModel : ViewModel() {
    private val _numStep = MutableLiveData(0)
    val numStep: LiveData<Int> = _numStep
    fun setNumStep(numStep: Int) {
        _numStep.value = numStep
    }

    private val _currentStepCounter = MutableLiveData(0f)
    val currentStepCounter: LiveData<Float> = _currentStepCounter
    fun setCurrentStepCounter(currentStepCounter: Float) {
        _currentStepCounter.value = currentStepCounter
    }

    private val _totalStep = MutableLiveData(0f)
    val totalStep: LiveData<Float> = _totalStep
    fun setTotalStep(totalStep: Float) {
        _totalStep.value = totalStep
    }

    private val _level = MutableLiveData(0)
    private val level: LiveData<Int> = _level
    fun setLevel(level: Int) {
        _level.value = level
    }

    private val _sumStepLevel = MutableLiveData(0)
    val sumStepLevel: LiveData<Int> = _sumStepLevel
    fun setSumStepLevel(sumStepLevel: Int) {
        _sumStepLevel.value = sumStepLevel
    }

    private val _isClickBtnPause = MutableLiveData(false)
    val isClickBtnPause: LiveData<Boolean> = _isClickBtnPause
    fun setIsClickBtnPause(clicked: Boolean) {
        _isClickBtnPause.value = clicked
    }

    private val _isShowTvPause = MutableLiveData(false)
    val isShowTvPause: LiveData<Boolean> = _isShowTvPause
    fun setIsShowTvPause(isShow: Boolean) {
        _isShowTvPause.value = isShow
    }

    private val _userId = MutableLiveData<Long>()
    val userId: LiveData<Long> get() = _userId
    fun setUserId(userId : Long) {
        _userId.value = userId
    }

    val distance = MutableLiveData<Float>()
}