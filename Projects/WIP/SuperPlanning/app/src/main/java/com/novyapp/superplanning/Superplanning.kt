package com.novyapp.superplanning

import android.app.Application
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class Superplanning : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
//        Firebase.database.setPersistenceEnabled(true)

    }
}