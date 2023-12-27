package com.example.stepcount

import android.annotation.SuppressLint
import com.example.stepcount.containers.MyApplication
import java.text.SimpleDateFormat
import java.util.*

object DateHelper {
    private const val LAST_DATE_KEY = "last_date"
    private const val DATE_FORMAT = "yyyy-MM-dd"

    fun saveLastDate(nameDay: String){
        MyApplication.saveDataString(LAST_DATE_KEY, nameDay)
    }

    @SuppressLint("SimpleDateFormat")
    fun getLastDate(): String? {
        val lastDate = MyApplication.loadDataString(LAST_DATE_KEY, "")
        return lastDate.ifEmpty {
            null
        }
    }

    fun isSameDay(dayOne: String, dayTwo: String): Boolean {
        return dayOne == dayTwo
    }

    fun name(day: Int, month: Int, year: Int): String {

        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.MONTH, month - 1)
            set(Calendar.YEAR, year)
        }
        return SimpleDateFormat("EE", Locale.ENGLISH).format(calendar.time)
    }
    fun nameDayNow(day: Int, month : Int, year: Int): String {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.MONTH, month - 1)
            set(Calendar.YEAR, year)
        }
        return SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(calendar.time)
    }
}