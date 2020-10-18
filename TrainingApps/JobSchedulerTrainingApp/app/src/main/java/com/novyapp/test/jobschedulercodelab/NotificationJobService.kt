package com.novyapp.test.jobschedulercodelab

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.NotificationCompat

class NotificationJobService : JobService() {

    lateinit var notificationManager: NotificationManager
    private val PRIMARY_CHANNEL_ID = "primary_notification_channel"


    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        createNotificationChannel()
        val contentPendingIntent = PendingIntent.getActivity(
            this, 0, Intent(this, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
            .setContentTitle("Job Service")
            .setContentText("Your Job is running")
            .setContentIntent(contentPendingIntent)
            .setSmallIcon(R.drawable.ic_job_running)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)

        notificationManager.notify(0, builder.build())

        return false
    }

    private fun createNotificationChannel() {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                PRIMARY_CHANNEL_ID,
                "Job Service Notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.apply {
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                description = "Notifications from Job Service"
            }
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}