package com.example.stepcount.ui.profile.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stepcount.data.model.HistoryData

class HistoryViewModel:ViewModel() {
    private val dataHistory = MutableLiveData<List<HistoryData>>()


}