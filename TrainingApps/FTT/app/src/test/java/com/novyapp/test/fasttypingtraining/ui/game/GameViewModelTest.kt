package com.novyapp.test.fasttypingtraining.ui.game

import com.novyapp.test.fasttypingtraining.data.domain.DomainWord
import com.novyapp.test.fasttypingtraining.data.source.FakeWordsRepository
import org.junit.Before


class GameViewModelTest {
    private val word1 = DomainWord(word = "word1")
    private val word2 = DomainWord(word = "word2")
    private val word3 = DomainWord(word = "word3")
    private val word4 = DomainWord(word = "word4")

    private val words: MutableList<DomainWord> = mutableListOf(word1, word2, word3, word4)

    private lateinit var wordsRepository: FakeWordsRepository
    @Before
    fun initRepository(){
        wordsRepository = FakeWordsRepository(words)
    }

}