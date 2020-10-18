package com.example.android.guesstheword.screens.score

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel(finalScore: Int) : ViewModel() {

    private val _fScore = MutableLiveData<Int>()
    val fScore : LiveData<Int>
        get() = _fScore

    private val _eventPlayAgain = MutableLiveData<Boolean>()
    val eventPlayAgain : LiveData<Boolean>
        get() = _eventPlayAgain

    init {
        Log.i("ScoreViewModel", ("Final score : $finalScore"))
        _fScore.value = finalScore
        _eventPlayAgain.value = false
    }

    fun onPlayAgainPressed(){
        _eventPlayAgain.value = true
    }

    fun resetScore(){
        _eventPlayAgain.value = false
        _fScore.value = 0
    }



}