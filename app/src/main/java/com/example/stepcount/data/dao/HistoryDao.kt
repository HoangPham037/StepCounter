package com.example.stepcount.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.stepcount.data.model.HistoryData

@Dao
interface HistoryDao {
    @Insert
    suspend fun insertHistory(historyData: HistoryData)

    @Query("SELECT * FROM history_data ORDER BY date DESC")
    suspend fun getAllHistorySortedDate(): List<HistoryData>

    @Query("DELETE FROM history_data WHERE id = :id")
    suspend fun deleteHistoryHyId(id: Long)
}