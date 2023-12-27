package com.example.stepcount.data.di

import com.example.stepcount.data.viewmodel.AppViewModel
import com.example.stepcount.ui.gps.training.GpsViewModel
import com.example.stepcount.ui.home.HomeViewModel
import com.example.stepcount.ui.profile.update_profile.RecommendViewModel
import com.example.stepcount.ui.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AppViewModel(get()) }
    viewModel { GpsViewModel(application = get()) }
    viewModel { SplashViewModel() }
    viewModel { RecommendViewModel() }
    viewModel { HomeViewModel() }
}