package com.novyapp.test.fasttypingtraining.ui.results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.novyapp.test.fasttypingtraining.R
import com.novyapp.test.fasttypingtraining.databinding.ResultsFragmentBinding
import com.novyapp.test.fasttypingtraining.toolbar

class ResultsFragment : Fragment() {

    lateinit var binding: ResultsFragmentBinding
//    lateinit var args: ResultsFragmentArgs


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.results_fragment, container, false)
        var args = ResultsFragmentArgs.fromBundle(arguments!!)
        val viewModel by viewModels<ResultViewModel> {
            ResultViewModelFactory(args.score, args.countdownTimeInMilli)
        }
        args = ResultsFragmentArgs.fromBundle(arguments!!)


        val viewModelFactory = ResultViewModelFactory(args.score, args.countdownTimeInMilli)
//        viewModel = ViewModelProvider(this, viewModelFactory).get(ResultViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.wordsPerMinutesTextView.text = viewModel.wordsPerMinute.value.toString()

        binding.retryButton.setOnClickListener {
            this.findNavController().navigate(ResultsFragmentDirections.actionResultsFragmentToGameFragment())
        }
        binding.titleScreenButton.setOnClickListener {
            this.findNavController().navigate(ResultsFragmentDirections.actionResultsFragmentToMainFragment())
        }



        toolbar?.title = getString(R.string.resultFragmentTitle)
        return binding.root
    }
}