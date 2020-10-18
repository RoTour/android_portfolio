package com.novyapp.test.fasttypingtraining.ui.results

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class ResultViewModelFactory(
    val score: Int,
    val countdownTimeInMilli: Long
) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("unchecked_cast")
        if(modelClass.isAssignableFrom(ResultViewModel::class.java)){
            return ResultViewModel(score, countdownTimeInMilli) as T
        }
        throw IllegalArgumentException("wtf bro in ResultViewModelFactory bad Args..")
    }
}