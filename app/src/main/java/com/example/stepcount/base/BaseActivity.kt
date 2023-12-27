package com.example.stepcount.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding, VM: ViewModel>() : AppCompatActivity() {
    protected lateinit var binding : VB
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = getViewBinding()
        setupView()
        setupDataObserver()
    }
    abstract fun getViewBinding(): VB
    abstract fun setupView()
    abstract fun setupDataObserver()
}