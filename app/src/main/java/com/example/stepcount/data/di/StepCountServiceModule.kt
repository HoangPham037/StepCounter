package com.example.stepcount.data.di

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.stepcount.Constant
import com.example.stepcount.Constant.NOTIFICATION_CHANNEL_ID_STEP_COUNT
import com.example.stepcount.ui.main.MainActivity
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

fun provideMainActivityPendingIntents(context: Context): PendingIntent =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java).also {
                it.action = Constant.ACTION_PAUSE_SERVICE

            },
            PendingIntent.FLAG_MUTABLE
        )
    } else {
        PendingIntent.getActivity(
            context,
            0,

            Intent(context, MainActivity::class.java).also {
                it.action = Constant.ACTION_PAUSE_SERVICE
                it.flags  =  Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
            },
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
    }
fun provideBaseNotificationBuilders(context: Context, pendingIntent: PendingIntent) =
    NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID_STEP_COUNT)
        .setContentTitle("Foreground service")
        .setContentText("Steps: 0")
        .setContentIntent(pendingIntent)

val stepCountServiceModule = module {
    factory { provideMainActivityPendingIntents(androidContext()) }
    factory { provideBaseNotificationBuilders(androidContext(), get()) }
}