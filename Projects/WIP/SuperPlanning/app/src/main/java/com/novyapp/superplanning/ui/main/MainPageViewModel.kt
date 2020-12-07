package com.novyapp.superplanning.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.MutableData
import com.novyapp.superplanning.data.CourseV2
import com.novyapp.superplanning.data.FirebaseDataSource
import timber.log.Timber
import java.lang.IllegalArgumentException

class MainPageViewModel : ViewModel() {

    val courses: MutableLiveData<HashMap<String, MutableList<CourseV2>>> =
        FirebaseDataSource.getCoursesByPromo("TLS-INFO-B2-2020-2021")

}

class MainPageViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(MainPageViewModel::class.java))
            return MainPageViewModel() as T
        throw IllegalArgumentException("Bad args provided")
    }
}