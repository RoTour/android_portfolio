package com.novyapp.test.fasttypingtraining.ui.main

import com.novyapp.test.fasttypingtraining.data.source.FakeWordsRepository
import org.junit.Before

class MainViewModelTest {

    lateinit var viewModel: MainViewModel

    @Before
    fun init_viewModel(){
        viewModel = MainViewModel(FakeWordsRepository())
    }

}