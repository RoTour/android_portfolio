package com.novyapp.test.fasttypingtraining.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.novyapp.test.fasttypingtraining.data.Result
import com.novyapp.test.fasttypingtraining.data.domain.DomainWord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.apache.tools.ant.Task
import org.hamcrest.core.IsEqual
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import timber.log.Timber

@ExperimentalCoroutinesApi
class DefaultWordsRepositoryTest {

    private val word1 = DomainWord(word = "word1")
    private val word2 = DomainWord(word = "word2")
    private val word3 = DomainWord(word = "word3")
    private val word4 = DomainWord(word = "word4")

    private val localWords: List<DomainWord> = listOf(word1, word2)
    private val remoteWords: List<DomainWord> = listOf(word1, word2, word3, word4)

    private lateinit var wordsLocalDataSource: FakeDataSource
    private lateinit var wordsRemoteDataSource: FakeDataSource

    private lateinit var wordsRepository: DefaultWordsRepository

    @Before
    fun init() {
        wordsLocalDataSource = FakeDataSource(localWords.toMutableList())
        wordsRemoteDataSource = FakeDataSource(remoteWords.toMutableList())

        wordsRepository = DefaultWordsRepository(wordsLocalDataSource, wordsRemoteDataSource, Dispatchers.Unconfined)
    }

    @Test
    fun refreshWords_refreshFromRemoteDataSource() = runBlockingTest{
        // When
        val result = wordsRepository.getAllWords(true) as Result.Success
        // Then
        assertThat(result, IsEqual(Result.Success(remoteWords)))
    }

    @Test
    fun deleteAllWords_deleteAllWordsInLocalDataSource() = runBlockingTest{
        wordsRepository.deleteAllWords()
        val result = wordsRepository.getAllWords() as Result.Success
        assertThat(result.data, IsEqual(emptyList()))
    }

    @Test
    fun saveWords_saveWordsIntoLocalDataSource() = runBlockingTest {
        // Given
        val word5 = DomainWord(word = "word5")
        val word6 = DomainWord(word = "word6")
        // When
        wordsRepository.saveWords(word5, word6)
        // Then
        val actualList = wordsRepository.getAllWords(false) as Result.Success
        val expectedList = localWords.toMutableList()
        expectedList.addAll(arrayOf(word5,word6))
        assertThat(actualList.data, IsEqual(expectedList.toList()))
    }

    @Test
    fun observeWords_checkIfResultIsNotNull() = runBlockingTest {
        val observableData = wordsRepository.observeWords()
        assert(!((observableData.value as Result.Success).data.isNullOrEmpty()))
    }


}