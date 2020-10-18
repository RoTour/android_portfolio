package com.novyapp.test.fasttypingtraining.ui.game

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.novyapp.test.fasttypingtraining.ResourcesProvider
import com.novyapp.test.fasttypingtraining.data.source.WordsRepositoryInterface
import java.lang.IllegalArgumentException

class GameViewModelFactory(
    private val repository: WordsRepositoryInterface,
    private val resProvider: ResourcesProvider
) :ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("unchecked_cast")
        if(modelClass.isAssignableFrom(GameViewModel::class.java)){
            return GameViewModel(repository, resProvider) as T
        }
        throw IllegalArgumentException("wtf bro in GameViewModelFactory: Illegal argument")
    }
}