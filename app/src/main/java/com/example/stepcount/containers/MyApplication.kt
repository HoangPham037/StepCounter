package com.example.stepcount.containers

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.stepcount.data.di.repositoryModule
import com.example.stepcount.data.di.roomModule
import com.example.stepcount.data.di.serviceModule
import com.example.stepcount.data.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

/*
Created by hoang.pv on 2023-05-16
*/
class MyApplication : Application() {
    var context: Context? = null
    override fun onCreate() {
        super.onCreate()
        context = this
        sharedPreferences = applicationContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        editor = sharedPreferences?.edit()!!
        configureDI()

    }

    companion object {
        private var sharedPreferences: SharedPreferences? = null
        lateinit var editor: SharedPreferences.Editor

        fun saveData(key: String, value: Float) {
            editor.putFloat(key, value)
            editor.apply()
        }

        fun loadData(key: String, setDefault: Float): Float {
            return sharedPreferences?.getFloat(key, setDefault) ?: 0f
        }

        fun saveDataBoolean(key: String, value: Boolean) {
            editor.putBoolean(key, value)
            editor.apply()
        }

        fun loadDataBoolean(key: String, setDefault: Boolean): Boolean {
            return sharedPreferences?.getBoolean(key, setDefault) ?: false
        }

        fun saveDataString(key: String, value: String) {
            editor.putString(key, value)
            editor.apply()
        }

        fun loadDataString(key: String, setDefault: String): String {
            return sharedPreferences?.getString(key, setDefault) ?: ""
        }
    }

    private fun configureDI() {
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(
                listOf(
                    roomModule,
                    repositoryModule,
                    viewModelModule,
                    serviceModule
                )
            )
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}