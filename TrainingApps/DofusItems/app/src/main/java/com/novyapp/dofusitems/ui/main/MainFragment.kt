package com.novyapp.dofusitems.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.novyapp.dofusitems.databinding.MainFragmentBinding

class MainFragment : Fragment() {

//    companion object {
//        fun newInstance() = MainFragment()
//    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding =  MainFragmentBinding.inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application
        val viewModelFactory = MainViewModelFactory(application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(MainViewModel::class.java)


        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        val adapter = MainListAdapter()
        binding.recyclerView.adapter = adapter



        return binding.root
    }



}