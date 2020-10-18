package com.novyapp.test.utlimatetodolist.ui.stats

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.novyapp.test.utlimatetodolist.R
import com.novyapp.test.utlimatetodolist.UTDLApplication
import com.novyapp.test.utlimatetodolist.databinding.StatsFragmentBinding

class StatsFragment : Fragment() {

    companion object {
        fun newInstance() = StatsFragment()
    }

    private val viewModel by viewModels<StatsViewModel> {
        StatsViewModelFactory(
            (requireContext().applicationContext as UTDLApplication).repository,
            (requireContext().applicationContext as UTDLApplication).resourceProvider
        )
    }
    private lateinit var binding: StatsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = StatsFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        return binding.root
    }



}