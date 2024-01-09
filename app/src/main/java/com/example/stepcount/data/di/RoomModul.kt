package com.example.stepcount.data.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.stepcount.data.dao.UserDao
import com.example.stepcount.data.database.AppDatabase
import org.koin.dsl.module

fun providesDatabase(application: Application): AppDatabase =
    Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "app_databases"
    ).fallbackToDestructiveMigration().build()


val roomModule = module {
    single { providesDatabase(get()) }
}