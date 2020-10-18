package com.novyapp.test.fasttypingtraining.ui.results

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber

class ResultViewModel(
    score: Int,
    countdownTimeInMilli: Long
) : ViewModel(){

    val wordsPerMinute = MutableLiveData<Float>()


    init {
        wordsPerMinute.value = ((60 * score) / (countdownTimeInMilli / 1000)).toFloat()
        Timber.i("Calculated WpM = ${wordsPerMinute.value}")
    }
}