package com.novyapp.test.fasttypingtraining.data.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.novyapp.test.fasttypingtraining.data.Result
import com.novyapp.test.fasttypingtraining.data.source.local.DatabaseHighScore
import com.novyapp.test.fasttypingtraining.data.source.local.DatabaseWords
import com.novyapp.test.fasttypingtraining.data.source.remote.NetworkHighScore

data class DomainHighScore(
    val playerId: Long = 0L,
    val wordsPerMinute: Double = 0.0
)

data class DomainWord(
    val wordId: Long = 0L,
    val word: String = ""
)

data class WordsSetup(
    var current: MutableLiveData<DomainWord> = MutableLiveData(DomainWord(word = "")),
    var next: MutableLiveData<DomainWord> = MutableLiveData(DomainWord(word = "")),
    var preloadedList: MutableList<DomainWord> = mutableListOf()
)

data class CompactResult(
    var data: Result<List<DomainWord>>? = null
)


fun List<DomainHighScore>.asDatabaseHighScore(): List<DatabaseHighScore>{
    return map {
        DatabaseHighScore(
            playerId = it.playerId,
            wordsPerMinute = it.wordsPerMinute
        )
    }
}

fun List<DomainHighScore>.asNetworkHighScore(): List<NetworkHighScore>{
    return map {
        NetworkHighScore(
            playerId = it.playerId,
            wordsPerMinute = it.wordsPerMinute
        )
    }
}

fun List<DomainWord>.asDatabaseWords(): Array<DatabaseWords>{
    return map {
        DatabaseWords(
            wordId = it.wordId,
            word = it.word
        )
    }.toTypedArray()
}

fun List<DomainWord>.asStrings(): List<String>{
    return map {
        it.word
    }
}

fun Array<out DomainWord>.asDatabaseWords(): Array<DatabaseWords>{
    return map {
        DatabaseWords(
            wordId = it.wordId,
            word = it.word
        )
    }.toTypedArray()
}

fun Result<List<DomainWord>>.asCompactResult(): CompactResult{
    return CompactResult(data = this)
}