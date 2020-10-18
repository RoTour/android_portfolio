package com.novyapp.test.fasttypingtraining.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.novyapp.test.fasttypingtraining.data.Result
import com.novyapp.test.fasttypingtraining.data.domain.DomainWord
import com.novyapp.test.fasttypingtraining.data.domain.WordsSetup
import com.novyapp.test.fasttypingtraining.data.source.remote.Network
import com.novyapp.test.fasttypingtraining.data.source.remote.asDatabaseWords
import com.novyapp.test.fasttypingtraining.data.source.remote.asDomainWords
import kotlinx.coroutines.*
import timber.log.Timber

class DefaultWordsRepository(
    private val wordsLocalDataSource: WordsDataSourceInterface,
    private val wordsRemoteDataSource: WordsDataSourceInterface,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : WordsRepositoryInterface {

    override var wordList: LiveData<Result<List<DomainWord>>> = observeWords()
    override var wordsSetup: LiveData<WordsSetup> = Transformations.map(wordList) {
        if (it is Result.Success && !it.data.isNullOrEmpty()) {
            WordsSetup(
                current = MutableLiveData(it.data.random()),
                next = MutableLiveData(it.data.random()),
                preloadedList = it.data.toMutableList()
            )
        } else if (it is Result.Success && it.data.isNullOrEmpty()) {
            Timber.i("Refreshing from remote")
            runBlocking {
                refreshWords()
            }
            WordsSetup()
        } else {
            WordsSetup()
        }
    }
    override var stateIsDataLoaded: MutableLiveData<Boolean> = MutableLiveData()


    init {
        Timber.i("initializing repo...")
    }


    override suspend fun getAllWords(forceRefresh: Boolean): Result<List<DomainWord>> {
        if (forceRefresh) {
            try {
                refreshWordsFromRemote()
            } catch (e: Exception) {
                return Result.Error(e)
            }
        }
        return wordsLocalDataSource.getAllWords()
    }

    override fun observeWords(): LiveData<Result<List<DomainWord>>> {
        return wordsLocalDataSource.observeWords()
    }

    override suspend fun saveWords(vararg words: DomainWord) {
        wordsLocalDataSource.saveWords(*words)
    }

    override suspend fun getRandomWords(numOfWords: Int): Result<List<DomainWord>> {
        return wordsLocalDataSource.getRandomWords(numOfWords)
    }

    override suspend fun deleteAllWords() {
        withContext(ioDispatcher) {
            coroutineScope {
                launch { wordsLocalDataSource.deleteAllWords() }
                launch { wordsRemoteDataSource.deleteAllWords() }
            }
        }
    }




    override suspend fun refreshWords() {
        refreshWordsFromRemote()
    }


    override suspend fun deleteOnWord(word: String) {
//        TODO("To implement if needed")
    }

    private suspend fun refreshWordsFromRemote() {
        withContext(ioDispatcher){
            try {
                val refreshedList = wordsRemoteDataSource.getAllWords()
                if (refreshedList is Result.Success<List<DomainWord>>) {
                    wordsLocalDataSource.deleteAllWords()
                    wordsLocalDataSource.saveWords(refreshedList)
                } else if (refreshedList is Result.Error) {
                    Timber.e(refreshedList.exception)
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }


}
