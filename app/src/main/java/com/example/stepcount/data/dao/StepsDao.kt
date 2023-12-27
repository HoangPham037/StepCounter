package com.example.stepcount.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.stepcount.data.model.HistoryData
import com.example.stepcount.data.model.StepsData

@Dao
interface StepsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSteps(stepsData: StepsData)

    //    @Query("SELECT * from steps_data WHERE CAST((date / 1000) AS INTEGER) BETWEEN strftime('%s','now','-6 days') AND strftime('%s','now')  ORDER BY id DESC")
//    suspend fun getStepDataFromLastSevenDays(): List<StepsData>
    @Query("SELECT * FROM steps_data WHERE day=:day AND month=:month AND year=:year")
    fun getStep(day: Int, month: Int, year: Int): StepsData

    @Query("SELECT * FROM ( SELECT * FROM steps_data ORDER BY id DESC LIMIT 7 ) sub ORDER BY id ASC")
    suspend fun getWeekSteps(): List<StepsData>

    // Get month steps method for dao of the room

    @Query("SELECT * FROM ( SELECT * FROM steps_data ORDER BY id DESC LIMIT 30 ) sub ORDER BY id ASC")
    fun getMonthSteps(): List<StepsData>

    @Query("SELECT * FROM steps_data ORDER BY id DESC")
    suspend fun getAllStepsSortedId(): List<StepsData>
}