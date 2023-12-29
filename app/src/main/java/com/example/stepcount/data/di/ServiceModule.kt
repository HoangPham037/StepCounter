package com.example.stepcount.data.di

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.stepcount.Constant.ACTION_SHOW_TRACKING_FRAGMENT
import com.example.stepcount.Constant.NOTIFICATION_CHANNEL_TRACKING_ID
import com.example.stepcount.R
import com.example.stepcount.ui.main.MainActivity
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

fun provideFusedLocationProviderClient(context: Context) =
    LocationServices.getFusedLocationProviderClient(context)

fun provideMainActivityPendingIntent(context: Context): PendingIntent =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java).also {
                it.action = ACTION_SHOW_TRACKING_FRAGMENT
            },
            PendingIntent.FLAG_MUTABLE
        )
    } else {
        PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java).also {
                it.action = ACTION_SHOW_TRACKING_FRAGMENT
            },
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

fun provideBaseNotificationBuilder(context: Context, pendingIntent: PendingIntent) =
    NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_TRACKING_ID)
        .setAutoCancel(false)
        .setOngoing(true)
        .setSmallIcon(R.drawable.step_count_logo)
        .setContentTitle("Steps counts App")
        .setContentIntent(pendingIntent)

val serviceModule = module {
    factory { provideFusedLocationProviderClient(androidContext()) }
    factory { provideMainActivityPendingIntent(androidContext()) }
    factory { provideBaseNotificationBuilder(androidContext(), get()) }
}

