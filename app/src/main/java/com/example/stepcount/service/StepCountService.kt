package com.example.stepcount.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
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
import androidx.lifecycle.MutableLiveData
import com.example.stepcount.R
import com.example.stepcount.containers.MyApplication
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*
import timber.log.Timber

class StepCountService : Service(), SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var running = false
    private var totalSteps = 0f
    private var previousTotalSteps = 0f
    private var currentStepCounter = 0
    private val notificationId = 1001
    private lateinit var currentDate: String
    private val channelId = "Notification from Service"
    private val stepRunInSecond = MutableLiveData<Float>()
    private val notificationBuilder: NotificationCompat.Builder by inject()
    private lateinit var curNotification: NotificationCompat.Builder


    private fun postInitialValues(){
        stepRunInSecond.postValue(0F)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        loadData()
        super.onCreate()
        postInitialValues()
        curNotification = notificationBuilder
        // Adding a context of SENSOR_SERVICE aas Sensor Manager
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        running = true
        startForeground()
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor == null)
            Toast.makeText(this, "No sensor detected on this device", Toast.LENGTH_SHORT).show()
        else
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        return START_NOT_STICKY
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(running && event?.sensor?.type == Sensor.TYPE_STEP_COUNTER){
            val newDate = getCurrentDate()
            if (newDate != getCurrentDate()){
                totalSteps = 0f
                currentDate = newDate
                // Reset any UI or perform any operations related to the date change
                Timber.tag("Hoang.pv@extremevn.com").i("Date changed. Resetting step count...")
            }
            totalSteps = event.values[0]
            currentStepCounter = totalSteps.toInt() - previousTotalSteps.toInt()
            stepRunInSecond.postValue(currentStepCounter.toFloat())
            Timber.tag("Hoang.pv@extremevn.com").i("current step from service : $currentStepCounter")
        }
    }
    private fun getCurrentDate():String{
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    @SuppressLint("RemoteViewLayout")
    private fun startForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
                channel
            )
        }



        val contentView = RemoteViews(packageName, R.layout.custom_notification)
        contentView.setTextViewText(R.id.tv_steps,"$currentStepCounter" )
        contentView.setTextViewText(R.id.tvCalories, "0.0")
        val notification = notificationBuilder.setCustomContentView(contentView)
        startForeground(notificationId, notification.build())
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager?.unregisterListener(this)
    }

    private fun loadData() {
        val previousStep = MyApplication.loadData("previousStep", 0f)
        previousTotalSteps = previousStep
    }
}