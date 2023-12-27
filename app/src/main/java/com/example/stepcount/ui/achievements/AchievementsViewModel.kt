package com.example.stepcount.ui.achievements

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stepcount.R
import com.example.stepcount.ui.achievements.model.DataCommon
import com.example.stepcount.ui.achievements.model.StepsLevelData


class AchievementsViewModel : ViewModel() {
    private val _dataSumDistance = MutableLiveData<List<DataCommon>>()
    val dataSumDistance: LiveData<List<DataCommon>> get() = _dataSumDistance

    fun fetchListDistance() {
        _dataSumDistance.value = setListDistance()
    }

    private val _dataDailyStep = MutableLiveData<List<DataCommon>>()
    val dataDailySteps: LiveData<List<DataCommon>> get() = _dataDailyStep
    fun fetchListDailyStep() {
        _dataDailyStep.value = setListStepDaily()
    }

    private val _dataListLevelStep = MutableLiveData<List<StepsLevelData>>()
    val dataListStepsLevel : LiveData<List<StepsLevelData>> get() = _dataListLevelStep
    fun fetchListStepsLevel() {
        _dataListLevelStep.value = setListStepsLevel()
    }
}

private fun setListStepsLevel(): List<StepsLevelData> {
    val levelOne = StepsLevelData(
        0f,
         0,
        "L1",
        lock = R.drawable.baseline_lock_24
    )
    val levelTwo = StepsLevelData(
        10000f,
         10,
        "L2",
        lock = R.drawable.baseline_lock_24
    )
    val levelThree = StepsLevelData(
        50000f,
        50,
        "L3",
        lock = R.drawable.baseline_lock_24
    )
    val levelFour = StepsLevelData(
        100000f,
        100,
        "L4",
        lock = R.drawable.baseline_lock_24
    )
    val levelFive = StepsLevelData(
        150000f,
         150,
        "L5",
        lock = R.drawable.baseline_lock_24
    )
    val levelSix = StepsLevelData(
        200000f,
        200,
        "L6",
        lock = R.drawable.baseline_lock_24
    )
    val levelSeven = StepsLevelData(
        400000f,
        400,
        "L7",
        lock = R.drawable.baseline_lock_24
    )
    val levelEight = StepsLevelData(
        600000f,
       600,
        "L8",
        lock = R.drawable.baseline_lock_24
    )
    val levelNight = StepsLevelData(
        1000000f,
        1000,
        "L9",
        lock = R.drawable.baseline_lock_24
    )
    val levelTen = StepsLevelData(
        1200000f,
       1200,
        "L10",
        lock = R.drawable.baseline_lock_24
    )
    return listOf(
        levelOne,
        levelTwo,
        levelThree,
        levelFour,
        levelFive,
        levelSix,
        levelSeven,
        levelEight,
        levelNight,
        levelTen
    )
}

private fun setListStepDaily(): List<DataCommon> {
    val dailyOne = DataCommon(3)
    val dailyTwo = DataCommon(7)
    val dailyThree = DataCommon(10)
    val dailyFour = DataCommon(14)
    val dailyFive = DataCommon(20)
    val dailySix = DataCommon(30)
    val dailySeven = DataCommon(40)
    val dailyEight = DataCommon(50)
    val dailyNight = DataCommon(60)
    return listOf(
        dailyOne, dailyTwo,
        dailyThree, dailyFour,
        dailyFive, dailySix,
        dailySeven, dailyEight,
        dailyNight
    )
}

private fun setListDistance(): List<DataCommon> {
    val distanceOne = DataCommon(5)
    val distanceTwo = DataCommon(10)
    val distanceThree = DataCommon(20)
    val distanceFour = DataCommon(50)
    val distanceFive = DataCommon(100)
    val distanceSix = DataCommon(220)
    val distanceSeven = DataCommon(440)
    val distanceEight = DataCommon(800)
    val distanceNight = DataCommon(2000)
    return listOf(
        distanceOne, distanceTwo,
        distanceThree, distanceFour,
        distanceFive, distanceSix,
        distanceSeven, distanceEight,
        distanceNight
    )
}