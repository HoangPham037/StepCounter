package com.example.stepcount.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stepcount.DateHelper
import com.example.stepcount.data.model.StepsData
import java.text.SimpleDateFormat
import java.time.Month
import java.util.*
import kotlin.collections.ArrayList

class HomeViewModel :ViewModel() {

    private val _target = MutableLiveData<Float>()
    val target: LiveData<Float> get() = _target
    fun setTarget(target: Float) {
        _target.value = target
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
            StepsData(0f,DateHelper.name(day, month, year), day, month, year, 0f, 0f,0, false)
        )
    }
}