package com.example.stepcount.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.example.stepcount.Constant.ACTION_OPEN_HOME_FRAGMENT
import com.example.stepcount.Constant.NOTIFICATION_CHANNEL_STEP_COUNT_ID
import com.example.stepcount.Constant.NOTIFICATION_CHANNEL_STEP_COUNT_NAME
import com.example.stepcount.Constant.REQUEST_CODE_STEP_COUNT
import com.example.stepcount.R
import com.example.stepcount.containers.MyApplication
import com.example.stepcount.ui.main.MainActivity
import java.text.SimpleDateFormat
import java.util.*

class StepCountService : LifecycleService(), SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var running = false
    private var totalSteps = 0f
    private var previousTotalSteps = 0f
    private var currentStepCounter = 0
    private val notificationId = 1001
    private lateinit var currentDate: String
    private val stepRunInSecond = MutableLiveData<Float>()
    private lateinit var pendingIntent: PendingIntent
    private var goals = 0
    private var calories = 0f

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        loadData()
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
//    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
//        running = true
//        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
//        if (stepSensor == null)
//            Toast.makeText(this, "No sensor detected on this device", Toast.LENGTH_SHORT).show()
//        else
//            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
//        startForeground()
//        return START_STICKY
//    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (running && event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            val newDate = getCurrentDate()
            if (newDate != getCurrentDate()) {
                totalSteps = 0f
                currentDate = newDate
            }
            totalSteps = event.values[0]
            currentStepCounter = totalSteps.toInt() - previousTotalSteps.toInt()
            stepRunInSecond.postValue(currentStepCounter.toFloat())
        }
    }

    @SuppressLint("RemoteViewLayout")
    private fun startForeground() {
        createNotificationChannel()
        val contentView = RemoteViews(packageName, R.layout.custom_notification)
        calories = calculateCaloriesCovered(currentStepCounter.toFloat())
        contentView.setTextViewText(R.id.tv_steps, "$currentStepCounter")
        contentView.setTextViewText(
            R.id.tvCalories,
            String.format("%.2f", calories).replace(",", ".")
        )
        contentView.setProgressBar(R.id.progress, 5000, currentStepCounter, false)

        val intent = Intent(this, MainActivity::class.java).also {
            it.action = ACTION_OPEN_HOME_FRAGMENT
        }

        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                this,
                REQUEST_CODE_STEP_COUNT,
                intent,
                PendingIntent.FLAG_MUTABLE
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

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_STEP_COUNT_ID,
                NOTIFICATION_CHANNEL_STEP_COUNT_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    override fun onBind(intent: Intent): IBinder? {
        return super.onBind(intent)
    }
//    override fun onBind(p0: Intent?): IBinder? {
//        return null
//    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager?.unregisterListener(this)
    }

    private fun loadData() {
        val previousStep = MyApplication.loadData("previousStep", 0f)
        previousTotalSteps = previousStep
    }

    private fun calculateCaloriesCovered(steps: Float): Float {
        return ((steps * 0.033).toFloat())
    }
}