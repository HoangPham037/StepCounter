package com.example.stepcount.data.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stepcount.DateHelper
import com.example.stepcount.data.model.HistoryData
import com.example.stepcount.data.model.StepsData
import com.example.stepcount.data.model.User
import com.example.stepcount.data.repository.AppRepository
import java.time.Month
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class AppViewModel(private val appRepository: AppRepository) : ViewModel() {



    private val _userLiveData = MutableLiveData<User>()
    val userLiveData: LiveData<User> = _userLiveData

    fun getUserById(id: Long) {
        viewModelScope.launch {
            val user = appRepository.getUserById(id)
            _userLiveData.value = user
        }
    }

    private val _registeredUserId: MutableLiveData<Long?> = MutableLiveData()
    val registeredUserId: LiveData<Long?> = _registeredUserId

    fun fetchRegisterUserId() {
        viewModelScope.launch {
            try {
                _registeredUserId.value = appRepository.getUserData()?.id
            } catch (e: Exception) {
                Timber.d("ERROR ${e.message}")
            }
        }
    }

    init {
        fetchRegisterUserId()
    }

    fun saveUser(user: User) {
        viewModelScope.launch {
            appRepository.insertUser(user)
        }
    }

    fun updateGoalByUserId(id: Long, goal: Int) {
        viewModelScope.launch {
            appRepository.updateGoalUserId(id, goal)
        }
    }

    fun updateGenderByUserId(id: Long, gender: String) {
        viewModelScope.launch {
            appRepository.updateGenderByUserId(id, gender)
        }
    }

    fun updateStepLengthByUserId(id: Long, stepLength: Int) {
        viewModelScope.launch {
            appRepository.updateStepLengthByUserId(id, stepLength)
        }
    }

    fun updateHeightByUserId(id: Long, height: Int) {
        viewModelScope.launch {
            appRepository.updateHeightByUserId(id, height)
        }
    }

    fun updateWeightByUserId(id: Long, weight: Double) {
        viewModelScope.launch {
            appRepository.updateWeightByUserId(id, weight)
        }
    }

    fun saveSteps(stepsData: StepsData) {
        viewModelScope.launch {
            appRepository.insertSteps(stepsData)
        }
    }


    private val _itemLastSevenDate = MutableLiveData<List<StepsData?>>()
    val itemLastSevenDate: LiveData<List<StepsData?>> get() = _itemLastSevenDate

//    fun fetchItemLastSevenDate() {
//        viewModelScope.launch {
//            val stepsData = appRepository.getStepDataFromLastSevenDays()
//            _itemLastSevenDate.value = stepsData
//        }
//    }

    fun saveHistory(historyData: HistoryData) {
        viewModelScope.launch {
            appRepository.insertHistoryData(historyData)
        }
    }

    private val _itemHistory = MutableLiveData<List<HistoryData>>()
    val itemHistory: LiveData<List<HistoryData>> get() = _itemHistory
    fun fetchItemHistory() {
        viewModelScope.launch {
            val historyData = appRepository.getAllHistorySortedDate()
            _itemHistory.value = historyData
        }
    }

    fun deleteHistoryById(id: Long) {
        viewModelScope.launch {
            appRepository.deleteHistoryById(id)
        }
    }

    private val _steps = MutableLiveData<List<StepsData>> ()
    val step : LiveData<List<StepsData>> get() = _steps

    fun fetchItemSteps(){
        viewModelScope.launch {
            val listStep = appRepository.getWeekSteps()
            _steps.value = listStep
        }
    }

    private val _stepsMonth = MutableLiveData<List<StepsData>> ()
    val stepsMonth : LiveData<List<StepsData>> get() = _stepsMonth
    fun fetchItemStepsMonth(){
        viewModelScope.launch {
            val listStepMonth = appRepository.getMonthSteps()
            _stepsMonth.value = listStepMonth
        }
    }

    private val _stepsAll = MutableLiveData<List<StepsData>> ()
    val stepsAll : LiveData<List<StepsData>> get() = _stepsAll
    fun fetchItemStepsAll(){
        viewModelScope.launch {
            val listStepMonth = appRepository.getMonthSteps()
            _stepsAll.value = listStepMonth
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun create(temp: ArrayList<StepsData>) : ArrayList<StepsData> {
        while (temp.size < 7) {
            val last = temp.last()
            val february = last.month == (Month.FEBRUARY).value
            val january = last.month == (Month.JANUARY).value
            when (last.day) {
                28 -> {
                    if (february && leapYear(last.year!!)) {
                        temp.add(last.id, 29, last.month!!, last.year!!)
                    } else if (february) {
                        temp.add(last.id, 1, last.month!! + 1, last.year!!)
                    } else {
                        temp.add(last.id, 29, last.month!!, last.year!!)
                    }
                }

                29 -> {
                    if (february) {
                        temp.add(last.id, 1, last.month!! + 1, last.year!!)
                    } else {
                        temp.add(last.id, 30, last.month!!, last.year!!)
                    }
                }

                30 -> {
                    if (month(last.month!!)) {
                        temp.add(last.id, 31, last.month!!, last.year!!)
                    } else {
                        temp.add(last.id, 1, last.month!! + 1, last.year!!)
                    }
                }

                31 -> {
                    if (january) {
                        temp.add(last.id, 1, 1, last.year!! + 1)
                    } else {
                        temp.add(last.id, 1, last.month!! + 1, last.year!!)
                    }
                }

                else -> { temp.add(last.id, last.day!! + 1, last.month!!, last.year!!) }
            }

        }
        return temp
    }
    private fun month(month: Int): Boolean = month in listOf(1, 3, 5, 7, 8, 10, 12)

    private fun leapYear(year: Int): Boolean = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)

    private fun ArrayList<StepsData>.add(id: Int, day: Int, month: Int, year: Int) {
        add(
            StepsData(0f, DateHelper.name(day, month, year), day, month, year, 0f, 0f,0, false)
        )
    }
}