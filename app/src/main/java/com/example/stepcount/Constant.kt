package com.example.stepcount

import android.graphics.Color
import java.text.NumberFormat
import java.util.*

object Constant {
    fun formatNumStep(steps: Int): String {
        return "$steps : step left"
    }
    fun formatStringWeight(value: Int, unit: String) = String.format("${value/10}.${value%10} $unit")
    fun formatString(value: Int, unit: String) = String.format("$value $unit")
    fun replaceString(unit: String, level: Int) = String.format("%s %s", unit, " more steps to reach level $level")
    fun formatNumber(value: Int): String = NumberFormat.getNumberInstance(Locale.US).format(value)

    fun formatInt(value: Int) = String.format("Avegare: $value")

    const val LOCATION_UPDATE_INTERVAL = 5000L
    const val FASTEST_LOCATION_INTERVAL = 2000L
    const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
    const val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"
    const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
    const val ACTION_SHOW_TRACKING_FRAGMENT = "ACTION_SHOW_TRACKING_FRAGMENT"
    const val ACTION_OPEN_HOME_FRAGMENT = "ACTION_OPEN_HOME_FRAGMENT"

    const val NOTIFICATION_CHANNEL_ID_STEP_COUNT = "Notification_from_Service"
    const val NOTIFICATION_CHANNEL_ID = "tracking_channel"
    const val NOTIFICATION_CHANNEL_NAME = "tracking"
    const val NOTIFICATION_ID = 1
    const val TIMER_UPDATE_INTERVAL = 50L
    const val MAP_ZOOM = 18f
    const val POLYLINE_COLOR = Color.RED
    const val POLYLINE_WIDTH = 8f
    const val ACTIONS = "mActions"

    //
    const val VALUE_WEEK = 7
    const val PAUSE = "PAUSE"
    const val RESUME = "RESUME"
    const val TIME_DEFAULT = "00:00:00"

    //shareReference user id
    const val KEY_USER_ID = "user_id"
    const val VALUE_DEFAULT = 0f


    object IndexPage {
        const val indexOne =  0
        const val indexTwo =  1
        const val indexThree = 2
        const val indexFour =  3
        const val indexFive =  4
    }
}