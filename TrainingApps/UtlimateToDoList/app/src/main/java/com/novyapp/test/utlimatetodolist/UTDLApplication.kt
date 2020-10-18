package com.novyapp.test.utlimatetodolist

import android.app.Application
import android.os.Build
import androidx.work.*
import com.novyapp.test.utlimatetodolist.data.ITasksRepository
import com.novyapp.test.utlimatetodolist.providers.ResourcesProvider
import com.novyapp.test.utlimatetodolist.providers.ServiceLocator
import com.novyapp.test.utlimatetodolist.work.ReminderNotificationWorker
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import java.util.concurrent.TimeUnit

class UTDLApplication : Application() {

    val resourceProvider: ResourcesProvider
        get() = ResourcesProvider.provideResourceProvider(this)

    val repository: ITasksRepository
        get() = ServiceLocator.provideRepository(this)

    override fun onCreate() {
        super.onCreate()
        val applicationScope = Dispatchers.Default
        Timber.plant(Timber.DebugTree())
//        setupReminderNotification()
    }

    private fun setupReminderNotification() {
        Timber.i("worker: settingup the work")
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiresBatteryNotLow(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    setRequiresDeviceIdle(true)
            }
            .build()


        val repeatingRequest =
            PeriodicWorkRequestBuilder<ReminderNotificationWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()


        WorkManager.getInstance().enqueueUniquePeriodicWork(
            ReminderNotificationWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }
}