package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val NO_BUZZ_PATTERN = longArrayOf(0)

enum class BuzzType(val pattern: LongArray) {
    CORRECT(CORRECT_BUZZ_PATTERN),
    GAME_OVER(GAME_OVER_BUZZ_PATTERN),
    COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
    NO_BUZZ(NO_BUZZ_PATTERN)
}

class GameViewModel : ViewModel() {

    // The current word
    private val _word = MutableLiveData<String>()
    val word : LiveData<String>
        get() = _word

    // The current score
    private val _score = MutableLiveData<Int>()
    val score : LiveData<Int>
        get() = _score

    // Game Finish event
    private val _eventGameFinished = MutableLiveData<Boolean>()
    val eventGameFinished : LiveData<Boolean>
        get() = _eventGameFinished

    // The current time
    private val _currentTime = MutableLiveData<Long>()
    val currentTime : LiveData<Long>
        get() = _currentTime

    private val _eventBuzzing = MutableLiveData<BuzzType>()
    val eventBuzzing : LiveData<BuzzType>
        get() = _eventBuzzing

    val currentTimeString = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time).toString()
    }


    companion object {

        // These represent different important times
        // This is when the game is over
//        const val DONE = 0L
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 20000L
    }
    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>
    private var timer: CountDownTimer



    init{
        Log.i("GameViewModel", "ViewModel created")
        _eventGameFinished.value = false
        _eventBuzzing.value = BuzzType.NO_BUZZ
        resetList()
        _word.value = ""
        nextWord()
        _score.value = 0


        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND){

            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = millisUntilFinished/ ONE_SECOND
                if((_currentTime.value)!! == 10L){
                    _eventBuzzing.value = BuzzType.COUNTDOWN_PANIC
                }
            }

            override fun onFinish() {
                _eventGameFinished.value = true
            }

        }
        timer.start()

    }

    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            resetList()
        } else {
            _word.value = wordList.removeAt(0)
        }

    }

    fun onSkip() {
        _score.value = (_score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = (_score.value)?.plus(1)
        _eventBuzzing.value = BuzzType.CORRECT
        nextWord()
    }

    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    fun onGameFinishedComplete(){
        _eventBuzzing.value = BuzzType.GAME_OVER
        _eventGameFinished.value = false
    }

    fun onBuzzingComplete(){
        _eventBuzzing.value = BuzzType.NO_BUZZ
    }



    override fun onCleared() {
        Log.i("GameViewModel", "ViewModel cleared")
        super.onCleared()
        timer.cancel()
    }


}