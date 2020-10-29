package com.novyapp.superplanning.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.novyapp.superplanning.data.FirebaseDataSource
import timber.log.Timber
import java.lang.IllegalArgumentException

class MainPageViewModel : ViewModel() {
    //    val courses = {
//        val thisWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)
//        val counter = 0
//        while (counter < 10){
//            FirebaseDataSource.getCoursesFromPromo("INFO-B2", thisWeek)
//        }
//    }
//    val courses = FirebaseDataSource.getCoursesFromPromoByWeek("INFO-B2")
    val courses = FirebaseDataSource.getAllCoursesByPromo("INFO-B2")

    init {
        Timber.i("Init viewModel")
    }

}

class MainPageViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(MainPageViewModel::class.java))
            return MainPageViewModel() as T
        throw IllegalArgumentException("Bad args provided")
    }
}