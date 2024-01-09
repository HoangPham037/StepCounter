package com.example.stepcount.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.stepcount.Constant.ACTION_OPEN_HOME_FRAGMENT
import com.example.stepcount.Constant.KEY_DAY
import com.example.stepcount.Constant.KEY_PREVIOUS_TOTAL_STEP
import com.example.stepcount.Constant.NOTIFICATION_CHANNEL_STEP_COUNT_ID
import com.example.stepcount.Constant.NOTIFICATION_CHANNEL_STEP_COUNT_NAME
import com.example.stepcount.Constant.REQUEST_CODE_STEP_COUNT
import com.example.stepcount.Constant.VALUE_DEFAULT_DAY
import com.example.stepcount.Constant.VALUE_DEFAULT_PREVIOUS_TOTAL_STEP
import com.example.stepcount.R
import com.example.stepcount.containers.MyApplication
import com.example.stepcount.containers.MyApplication.Companion.loadIntData
import com.example.stepcount.containers.MyApplication.Companion.saveData
import com.example.stepcount.containers.MyApplication.Companion.saveIntData
import com.example.stepcount.ui.main.MainActivity
import com.example.stepcount.ui.splash.SplashFragment
import java.text.SimpleDateFormat
import java.util.*

class StepCountService : LifecycleService(), SensorEventListener,OnSharedPreferenceChangeListener {

    private var sensorManager: SensorManager? = null
    private var running = false
    private var totalSteps = 0f
    private var previousTotalSteps = 0f
    private var currentStepCounter = 0f
    private val notificationId = 1001
    private lateinit var currentDate: String
    private val stepRunInSecond = MutableLiveData<Float>()
    private lateinit var pendingIntent: PendingIntent
    private var goals = 0
    private var calories = 0f

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        loadPreviousTotalSteps()
        super.onCreate()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepRunInSecond.observe(this) {
            Toast.makeText(this, "Step = $it", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        running = true
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor == null) {
            Toast.makeText(this, "No sensor detected on this device", Toast.LENGTH_SHORT).show()
        }else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
            startForeground()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (running && event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            totalSteps = event.values[0]
            val currentTime = Calendar.getInstance()
            if (currentTime.get(Calendar.HOUR_OF_DAY) <= 23 &&
                currentTime.get(Calendar.MINUTE) <= 59 &&
                currentTime.get(Calendar.SECOND) <= 59) {
                loadPreviousTotalSteps()
                val currentStep = totalSteps - previousTotalSteps
                updateNotification(currentStep)

            }else {
                loadPreviousTotalSteps()
                previousTotalSteps = totalSteps
                saveData(KEY_PREVIOUS_TOTAL_STEP, previousTotalSteps)
            }
        }
    }
    private fun updateNotification(steps: Float) {
        // Update your notification with the new step count
        val contentView = RemoteViews(packageName, R.layout.custom_notification)
        calories = calculateCaloriesCovered(steps)
        contentView.setTextViewText(R.id.tv_steps, "$steps")
        contentView.setTextViewText(
            R.id.tvCalories,
            String.format("%.2f", calories).replace(",", ".")
        )
        contentView.setProgressBar(R.id.progress, 5000, steps.toInt(), false)

        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_STEP_COUNT_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.step_count_logo)
            .setContentTitle("Steps counts App")
            .setCustomContentView(contentView)
            .setContentIntent(pendingIntent)
        startForeground(notificationId, notification.build())
    }

    private fun startForeground() {
        createNotificationChannel()
        val contentView = RemoteViews(packageName, R.layout.custom_notification)
        calories = calculateCaloriesCovered(currentStepCounter.toFloat())
        contentView.setTextViewText(R.id.tv_steps, "$currentStepCounter")
        contentView.setTextViewText(
            R.id.tvCalories,
            String.format("%.2f", calories).replace(",", ".")
        )
        contentView.setProgressBar(R.id.progress, 5000, currentStepCounter.toInt(), false)

        val intent = Intent(this, MainActivity::class.java).also {
            it.action = ACTION_OPEN_HOME_FRAGMENT
        }
        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addNextIntentWithParentStack(intent)
        pendingIntent =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                this,
                REQUEST_CODE_STEP_COUNT,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            PendingIntent.getActivity(
                this,
                REQUEST_CODE_STEP_COUNT,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_STEP_COUNT_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.step_count_logo)
            .setContentTitle("Steps counts App")
            .setCustomContentView(contentView)
            .setContentIntent(pendingIntent)
        startForeground(notificationId, notification.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_STEP_COUNT_ID,
                NOTIFICATION_CHANNEL_STEP_COUNT_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    override fun onBind(intent: Intent): IBinder? {
        return super.onBind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager?.unregisterListener(this)
    }

    private fun loadPreviousTotalSteps() {
        val previousStep = MyApplication.loadData(KEY_PREVIOUS_TOTAL_STEP, VALUE_DEFAULT_PREVIOUS_TOTAL_STEP)
        previousTotalSteps = previousStep
    }

    private fun calculateCaloriesCovered(steps: Float): Float {
        return ((steps * 0.033).toFloat())
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        loadPreviousTotalSteps()
    }
}