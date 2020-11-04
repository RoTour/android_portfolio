package com.novyapp.superplanning.ui.addcourse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.novyapp.superplanning.R
import com.novyapp.superplanning.data.FirebaseDataSource
import com.novyapp.superplanning.databinding.AddCourseFragmentBinding

class AddCourseFragment : Fragment() {

    companion object {
        fun newInstance() = AddCourseFragment()
    }

    private lateinit var binding: AddCourseFragmentBinding
    private val viewModel by viewModels<AddCourseViewModel> {
        AddCourseViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddCourseFragmentBinding.inflate(inflater, container, false)

        binding.createCourseButton.setOnClickListener { createCourseWithInputs() }


        return binding.root
    }

    private fun createCourseWithInputs() {

    }

}