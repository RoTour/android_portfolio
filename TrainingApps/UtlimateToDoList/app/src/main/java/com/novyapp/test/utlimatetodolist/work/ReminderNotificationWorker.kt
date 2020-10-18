package com.novyapp.test.utlimatetodolist.work

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.text.format.DateFormat
import android.text.format.DateUtils
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.novyapp.test.utlimatetodolist.MainActivity
import com.novyapp.test.utlimatetodolist.R
import timber.log.Timber

private const val REMINDER_NOTIFICATION_CHANNEL_ID = "reminder_notification_channel_id"
private const val REQUEST_CODE = 0

class ReminderNotificationWorker (
    val appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params){

    companion object {
        val WORK_NAME = "reminder_notification_reminder"
    }

    init {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val notificationManager: NotificationManager = ContextCompat.getSystemService(
            appContext, NotificationManager::class.java
        ) as NotificationManager
        val channel: NotificationChannel = NotificationChannel(
            REMINDER_NOTIFICATION_CHANNEL_ID,
            appContext.getString(R.string.reminder_notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.apply {
            enableLights(true)
            lightColor = Color.GREEN
            enableVibration(true)
            description = appContext.getString(R.string.reminder_notification_channel_description)
        }

        notificationManager.createNotificationChannel(channel)
    }

    override suspend fun doWork(): Result {

        Timber.i("worker : The work is busy! ${DateUtils.formatElapsedTime(System.currentTimeMillis())}")

        val notificationManager: NotificationManager = ContextCompat.getSystemService(
            appContext, NotificationManager::class.java
        ) as NotificationManager

        val contentPendingIntent = PendingIntent.getActivity(
            appContext, REQUEST_CODE, Intent(appContext, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(
            appContext, REMINDER_NOTIFICATION_CHANNEL_ID
        )
            .setContentTitle(appContext.getString(R.string.reminder_notification_title))
            .setContentText(appContext.getString(R.string.reminder_notification_text))
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(contentPendingIntent)
            .setSmallIcon(R.drawable.ic_check_24)

        notificationManager.notify(REQUEST_CODE, builder.build())
        return Result.success()
    }
}