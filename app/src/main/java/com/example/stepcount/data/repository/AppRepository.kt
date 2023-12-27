package com.example.stepcount.data.repository

import com.example.stepcount.data.dao.HistoryDao
import com.example.stepcount.data.dao.StepsDao
import com.example.stepcount.data.dao.UserDao
import com.example.stepcount.data.model.HistoryData
import com.example.stepcount.data.model.StepsData
import com.example.stepcount.data.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository(
    private val userDao: UserDao,
    private val stepsDao: StepsDao,
    private val historyDao: HistoryDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun getUserById(id: Long): User {
        return withContext(dispatcher){
            userDao.getUserById(id)
        }
    }

    suspend fun updateGoalUserId(id: Long, goal:Int) = withContext(dispatcher){
        userDao.updateGoalByUserId(id,goal)
    }

    suspend fun updateGenderByUserId(id: Long, gender: String) = withContext(dispatcher){
        userDao.updateGenderByUserId(id,gender)
    }

    suspend fun updateStepLengthByUserId(id: Long, stepLength: Int) = withContext(dispatcher){
        userDao.updateStepLengthByUserId(id,stepLength)
    }

    suspend fun updateHeightByUserId(id: Long, height: Int) = withContext(dispatcher){
        userDao.updateHeightByUserId(id, height)
    }

    suspend fun updateWeightByUserId(id: Long, weight: Int) = withContext(dispatcher){
        userDao.updateWeightByUserId(id, weight)
    }

    suspend fun insertUser(user: User) = withContext(dispatcher){
        userDao.insertUser(user)
    }

    suspend fun getUserData(): User? {
        return withContext(dispatcher){
            userDao.getUserData()
        }
    }

    suspend fun insertSteps(stepsData: StepsData) = withContext(dispatcher){
        stepsDao.insertSteps(stepsData)
    }

//    suspend fun getStepDataFromLastSevenDays(): List<StepsData?>{
//        return withContext(dispatcher){
//            stepsDao.getStepDataFromLastSevenDays()
//        }
//    }
    suspend fun getWeekSteps(): List<StepsData>{
        return withContext(dispatcher) {
            stepsDao.getWeekSteps()
        }
    }

    suspend fun getMonthSteps(): List<StepsData> {
        return withContext(dispatcher) {
            stepsDao.getMonthSteps()
        }
    }

    suspend fun getAllSteps(): List<StepsData> {
        return withContext(dispatcher) {
            stepsDao.getAllStepsSortedId()
        }
    }
    suspend fun insertHistoryData(historyData: HistoryData) = withContext(dispatcher){
        historyDao.insertHistory(historyData)
    }

    suspend fun getAllHistorySortedDate(): List<HistoryData>{
        return withContext(dispatcher){
            historyDao.getAllHistorySortedDate()
        }
    }

    suspend fun deleteHistoryById(id: Long) = withContext(dispatcher){
        historyDao.deleteHistoryHyId(id)
    }

}