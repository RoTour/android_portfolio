package com.novyapp.test.fasttypingtraining.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.novyapp.test.fasttypingtraining.data.Result
import com.novyapp.test.fasttypingtraining.data.domain.CompactResult
import com.novyapp.test.fasttypingtraining.data.domain.DomainWord
import com.novyapp.test.fasttypingtraining.data.domain.WordsSetup
import com.novyapp.test.fasttypingtraining.data.domain.asCompactResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking

class FakeWordsRepository(words: MutableList<DomainWord> = mutableListOf()) : WordsRepositoryInterface {

    val data: MutableList<DomainWord> = words
    override var wordList: LiveData<Result<List<DomainWord>>> = observeWords()
    override var stateIsDataLoaded: MutableLiveData<Boolean> = MutableLiveData()
    override var wordsSetup: LiveData<WordsSetup> = Transformations.map(wordList) {
        if (it is Result.Success) {
            WordsSetup(
                current = MutableLiveData(it.data.random()),
                next = MutableLiveData(it.data.random()),
                preloadedList = it.data.toMutableList()
            )
        } else {
            WordsSetup()
        }
    }

    override suspend fun getAllWords(forceRefresh: Boolean): Result<List<DomainWord>> {
        return Result.Success(data.toList())
    }

    override suspend fun saveWords(vararg words: DomainWord) {
        data.addAll(words)
    }

    override suspend fun refreshWords() {
//        TODO("Not yet implemented")
    }

    override suspend fun getRandomWords(numOfWords: Int): Result<List<DomainWord>> {
        val list: MutableList<DomainWord> = mutableListOf()
        for (i in 0 until numOfWords) list.add(data.random())
        return Result.Success(list)
    }

    override suspend fun deleteAllWords() {
        data.clear()
    }

    override suspend fun deleteOnWord(word: String) {
//        TODO("Not yet implemented")
    }

    override fun observeWords(): LiveData<Result<List<DomainWord>>> {
        return MutableLiveData(Result.Success(data.toList()))
    }
}