package com.novyapp.test.fasttypingtraining

import android.app.Application
import android.content.Context
import com.novyapp.test.fasttypingtraining.BuildConfig
import com.novyapp.test.fasttypingtraining.ServiceLocator
import com.novyapp.test.fasttypingtraining.data.source.WordsRepositoryInterface
import timber.log.Timber

class WordsApplication : Application(){

    val wordsRepository: WordsRepositoryInterface
        get() = ServiceLocator.provideWordsRepository(this)
    val resourcesProvider: ResourcesProvider
        get() = ResourcesProvider.provideResourceProvider(this)


    val context: Context
        get() = ResourcesProvider.provideContext(this)

    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }


}