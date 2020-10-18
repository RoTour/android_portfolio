package com.novyapp.test.fasttypingtraining.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.novyapp.test.fasttypingtraining.*
import com.novyapp.test.fasttypingtraining.data.Result
import com.novyapp.test.fasttypingtraining.data.domain.DomainWord
import com.novyapp.test.fasttypingtraining.databinding.MainFragmentBinding
import timber.log.Timber


class MainFragment : Fragment(){

    lateinit var binding: MainFragmentBinding
//    lateinit var viewModel: MainViewModel

    private val viewModel by viewModels<MainViewModel> {
        MainViewModelFactory((requireContext().applicationContext as WordsApplication).wordsRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainFragmentBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

//        val repo = (requireContext().applicationContext as WordsApplication).wordsRepository

        binding.playButton.setOnClickListener {
            if(viewModel.checkIsDataLoaded()){
                this.findNavController().navigate(MainFragmentDirections.actionMainFragmentToGameFragment())
                Timber.i("Result is True")
            } else {
                this.findNavController().navigate(MainFragmentDirections.actionMainFragmentToLoadingFragment())
                Timber.i("Result is False")
            }
        }

        viewModel.wordList.observe(viewLifecycleOwner, Observer {
            Timber.i("observing wordList")
            if(it is Result.Success){
                Timber.i("wordsList is Success")
                if (!it.data.isNullOrEmpty()){
                    Timber.i("List length is: ${it.data.size}")
                }
            } else {
                Timber.i("wordsList is Not Success")
            }
        })

        viewModel.wordSetup.observe(viewLifecycleOwner, Observer {
            Timber.i("HERE IS WORD SETUP : current: ${it.current}, next: ${it.next}")
        })




        toolbar?.title = getString(R.string.mainFragmentTitle)
        return binding.root
    }


}