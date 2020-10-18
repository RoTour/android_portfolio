package com.novyapp.test.fasttypingtraining.ui.loading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.novyapp.test.fasttypingtraining.WordsApplication
import com.novyapp.test.fasttypingtraining.data.Result
import com.novyapp.test.fasttypingtraining.databinding.LoadingScreenFragmentBinding
import timber.log.Timber

class LoadingFragment : Fragment() {

    lateinit var binding: LoadingScreenFragmentBinding
    val viewModel by viewModels<LoadingViewModel> {
        LoadingViewModelFactory((requireContext().applicationContext as WordsApplication).wordsRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LoadingScreenFragmentBinding.inflate(inflater, container, false)


        viewModel.wordSetup.observe(viewLifecycleOwner, Observer {
            if (viewModel.checkIsDataLoaded()) {
                this.findNavController()
                    .navigate(LoadingFragmentDirections.actionLoadingFragmentToGameFragment())
            }
        })

        viewModel.wordSetup.observe(viewLifecycleOwner, Observer {
            Timber.i("HERE IS WORD SETUP : current: ${it.current.value}, next: ${it.next.value}")
        })
//        WordsRepository.repositoryStateInfo.observe(viewLifecycleOwner, Observer {
//            if(!it.isNullOrEmpty() && it.isNotBlank()){
//                binding.loadingScreenTextTextView.text = it
//            }
//        })

        return binding.root
    }
}