package com.novyapp.superplanning.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.novyapp.superplanning.data.FirebaseDataSource
import java.lang.IllegalArgumentException

class MainPageViewModel : ViewModel() {
    val courses = FirebaseDataSource.getCoursesFromPromo("INFO-B2")
}

class MainPageViewModelFactory(

) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(MainPageViewModel::class.java))
            return MainPageViewModel() as T
        throw IllegalArgumentException("Bad args provided")
    }
}