package com.novyapp.test.fasttypingtraining.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.novyapp.test.fasttypingtraining.data.Result
import com.novyapp.test.fasttypingtraining.data.domain.DomainWord

class FakeDataSource(var wordsList: MutableList<DomainWord> = mutableListOf()) : WordsDataSourceInterface {

    private val observableWords = MutableLiveData<Result<List<DomainWord>>>()
    override suspend fun getAllWords(): Result<List<DomainWord>> {
        return Result.Success(wordsList)
    }

    override suspend fun saveWords(vararg words: DomainWord) {
        for (word in words) { wordsList.add(word) }
    }

    override suspend fun saveWords(results: Result.Success<List<DomainWord>>) {
        for (it in results.data) { wordsList.add(it) }
    }

    override suspend fun getRandomWords(numOfWords: Int): Result<List<DomainWord>> {
        val list: MutableList<DomainWord> = mutableListOf()
        for (i in 0 until numOfWords) list.add(wordsList.random())
        return Result.Success(list)
    }

    override suspend fun refreshWords() {
//        TODO("Not yet implemented")
    }

    override suspend fun deleteAllWords() {
        wordsList.clear()
    }

    override suspend fun deleteOnWord(word: String) {
//        TODO("Not yet implemented")
    }

    override fun observeWords(): LiveData<Result<List<DomainWord>>> {
        return MutableLiveData(Result.Success(wordsList.toList()))
    }
}