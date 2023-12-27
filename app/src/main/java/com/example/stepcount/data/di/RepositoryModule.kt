package com.example.stepcount.data.di

import com.example.stepcount.data.database.AppDatabase
import com.example.stepcount.data.repository.AppRepository
import com.example.stepcount.data.dao.HistoryDao
import com.example.stepcount.data.dao.StepsDao
import com.example.stepcount.data.dao.UserDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

fun providesUserDao(db: AppDatabase): UserDao = db.userDao()
fun providesStepsDao(db: AppDatabase): StepsDao = db.stepsDao()
fun providesHistoryDao(db: AppDatabase): HistoryDao = db.historyDao()
fun providesDispatcher(): CoroutineDispatcher = Dispatchers.IO

val repositoryModule = module {
    single { providesUserDao(get()) }
    single { providesStepsDao(get()) }
    single { providesHistoryDao(get()) }
    single { providesDispatcher() }
    single { AppRepository(get(),get(),get(),get()) }
}