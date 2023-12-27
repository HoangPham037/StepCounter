package com.example.stepcount.ui.gps.training

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.stepcount.ui.gps.LocationLiveData

class GpsViewModel(application: Application) : AndroidViewModel(application) {
    private val locationData = LocationLiveData(application)
    fun getLocationData() = locationData
}