package com.novyapp.test.fasttypingtraining.ui.game

import android.os.CountDownTimer
import android.text.Editable
import android.text.format.DateUtils
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.novyapp.test.fasttypingtraining.R
import com.novyapp.test.fasttypingtraining.ResourcesProvider
import com.novyapp.test.fasttypingtraining.data.source.WordsRepositoryInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

enum class GameState {
    NOT_STARTED,
    IN_PROGRESS,
    FINISHED
}

const val ONE_SECOND = 1000L
const val COUNTDOWN_TIME = 30 * ONE_SECOND


class GameViewModel(
    private val repository: WordsRepositoryInterface,
    private val resProvider: ResourcesProvider
) : ViewModel() {



    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private lateinit var timer: CountDownTimer
    val currentTime = MutableLiveData<Long>()

    val currentTimeString = Transformations.map(currentTime){
//        if(it == 0L) "Start typing to start the game!"
//        else "Time remaining: ${DateUtils.formatElapsedTime(it)}s"
        if(it == -1L) resProvider.provideString(R.string.gameNotStarted)
        else resProvider.provideString(R.string.timeRemaining, DateUtils.formatElapsedTime(it))


    }
    val wordsSetup = repository.wordsSetup

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private val _gameState = MutableLiveData<GameState>()
    val gameState: LiveData<GameState>
        get() = _gameState

    private val _textColor = MutableLiveData<Int>()
    val textColor: LiveData<Int>
        get() = _textColor




    init {
        _score.value = 0
        _gameState.value = GameState.NOT_STARTED
        _textColor.value = resProvider.provideColor(R.color.primaryTextColor)
        currentTime.value = -1

        uiScope.launch {
            repository.refreshWords()
        }
    }

    private fun nextWord() {
        wordsSetup.value?.let { setup ->
            setup.current.value = setup.next.value
            setup.next.value = setup.preloadedList.random()
        }
    }

    private fun incrementScore() {
        _score.value = _score.value?.plus(1) ?: 0
    }

    fun startGame() {
        _gameState.value = GameState.IN_PROGRESS
        startTimer()
    }

    private fun closeGame() {
        timer.cancel()
        _gameState.value = GameState.FINISHED
    }
    
    @VisibleForTesting
    fun closeGameFromTest(){
        closeGame()
    }

    private fun startTimer() {
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onFinish() {
                closeGame()
            }

            override fun onTick(millisUntilFinished: Long) {
                currentTime.value = millisUntilFinished / ONE_SECOND
            }
        }
        timer.start()
    }

    override fun onCleared() {
        super.onCleared()
        if (_gameState.value == GameState.IN_PROGRESS) timer.cancel()
        viewModelJob.cancel()
    }

    private var isCorrect = true
    fun computeIsCorrect(s: Editable?) {
        s?.let {
            if (it.isNotEmpty()) {
                if (it.last() == ' ') {
                    if (isCorrect) incrementScore()
                    _textColor.value = resProvider.provideColor(R.color.primaryTextColor)
                    nextWord()
                    it.clear()
                    isCorrect = true
                } else {
                    isCorrect =
                        if ((it.length <= wordsSetup.value!!.current.value?.word!!.length) && (it.toString() == wordsSetup.value!!.current.value?.word!!.substring(
                                0,
                                it.length
                            ))
                        ) {
                            _textColor.value = resProvider.provideColor(R.color.primaryTextColor)
                            true
                        } else {
                            _textColor.value = resProvider.provideColor(R.color.errorTextColor)
                            false
                        }
                }
            }
        }
    }

}
