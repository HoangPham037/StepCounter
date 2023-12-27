package com.example.stepcount.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.stepcount.data.Converters
import com.example.stepcount.data.dao.HistoryDao
import com.example.stepcount.data.dao.StepsDao
import com.example.stepcount.data.dao.UserDao
import com.example.stepcount.data.model.HistoryData
import com.example.stepcount.data.model.StepsData
import com.example.stepcount.data.model.User

@Database(entities = [User::class, StepsData::class, HistoryData::class], version = 3)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao() : UserDao
    abstract fun stepsDao(): StepsDao
    abstract fun historyDao(): HistoryDao
}