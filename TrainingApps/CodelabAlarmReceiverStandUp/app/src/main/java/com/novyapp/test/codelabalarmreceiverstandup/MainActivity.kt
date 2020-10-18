package com.novyapp.test.codelabalarmreceiverstandup

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.provider.AlarmClock
import android.util.Log
import android.widget.Toast
import android.widget.ToggleButton
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

const val NOTIFICATION_ID = 0
const val PRIMARY_CHANNEL_ID = "primary_notification_channel"

class MainActivity : AppCompatActivity() {

    private lateinit var notificationManager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val alarmToggle: ToggleButton = findViewById(R.id.alarmToggle)

        notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager
        createChannel(getString(R.string.reminder_channel))

        val notifyIntent = Intent(this, AlarmReceiver::class.java)
        var alarmUp = PendingIntent.getBroadcast(
            this, NOTIFICATION_ID, notifyIntent,
            PendingIntent.FLAG_NO_CREATE
        ) != null





        Log.i("main", "State is : $alarmUp")
        alarmToggle.isChecked = alarmUp
        val notifyPendingIntent = PendingIntent.getBroadcast(
            this,
            NOTIFICATION_ID,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager



        alarmToggle.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                val repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES
                val triggerTime = SystemClock.elapsedRealtime() + repeatInterval
                alarmManager.setRepeating(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    triggerTime,
                    repeatInterval,
                    notifyPendingIntent
                )
            } else {
                notificationManager.cancelAll()
                notifyPendingIntent.cancel()
                alarmManager.cancel(notifyPendingIntent)
                alarmUp = PendingIntent.getBroadcast(
                    this, NOTIFICATION_ID, notifyIntent,
                    PendingIntent.FLAG_NO_CREATE
                ) != null
                Log.i("main", "State is : $alarmUp")
            }

        }

    }

    private fun createChannel(channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                PRIMARY_CHANNEL_ID,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableLights(true)
                lightColor = Color.GREEN
                enableVibration(true)
                description = "Notifies every 15 minutes to stand up and walk"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }


}