package com.novyapp.test.utlimatetodolist

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

private const val REMINDER_NOTIFICATION_CHANNEL_ID = "reminder_notification_channel_id"
private const val REQUEST_CODE = 0

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class ReminderJobService : JobService() {
    override fun onStopJob(p0: JobParameters?): Boolean {
        return true
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        createNotificationChannel()
        val notificationBuilder: NotificationCompat.Builder = createReminderNotification()
        val notificationManager: NotificationManager = getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager

        notificationManager.notify(REQUEST_CODE, notificationBuilder.build())


        return false
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                REMINDER_NOTIFICATION_CHANNEL_ID,
                getString(R.string.reminder_notification_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.apply {
                enableLights(true)
                lightColor = Color.GREEN
                enableVibration(true)
                description = getString(R.string.reminder_notification_channel_description)
            }
            val notificationManager = getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createReminderNotification(): NotificationCompat.Builder {
        val contentPendingIntent = PendingIntent.getActivity(
            this,
            REQUEST_CODE,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = NotificationCompat.Builder(
            this,
            REMINDER_NOTIFICATION_CHANNEL_ID
        )
            .setContentTitle(getString(R.string.reminder_notification_title))
            .setContentText(getString(R.string.reminder_notification_text))
            .setSmallIcon(R.drawable.ic_check_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(contentPendingIntent)
        return builder
    }


}