package com.novyapp.superplanning.ui.addcourse

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.novyapp.superplanning.databinding.AddCourseFragmentBinding
import timber.log.Timber
import java.util.*

class AddCourseFragment : Fragment() {

    companion object {
//        fun newInstance() = AddCourseFragment()
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

        setInputsListeners()

        binding.datePickerButton.setOnClickListener { showDateTimePickers() }

        binding.createCourseButton.setOnClickListener { createCourseWithInputs() }

        viewModel.uploadResult.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }

        return binding.root
    }


    // Ugly code
    private fun setInputsListeners() {
        binding.subjectEditText.addTextChangedListener {
            it?.let { viewModel.subject = it.toString() } ?: run { viewModel.subject = "" }
        }
        binding.classroomEditText.addTextChangedListener {
            it?.let { viewModel.classroom = it.toString() } ?: run { viewModel.classroom = "" }
        }
        binding.professorEditText.addTextChangedListener {
            it?.let { viewModel.professor = it.toString() } ?: run { viewModel.professor = "" }
        }
        binding.promotionEditText.addTextChangedListener {
            it?.let { viewModel.promotion = it.toString() } ?: run { viewModel.promotion = "" }
        }
    }

    private fun showDateTimePickers() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            requireContext(), { _, year1, monthOfYear, dayOfMonth ->
                // Creates a date with values provided from the Picker
                viewModel.day =
                    Calendar.getInstance()
                        .apply { set(year1, monthOfYear, dayOfMonth) }

                showTimePicker()
            }, year, month, day
        )

        dpd.show()
    }

    private fun showTimePicker() {
        val now = Calendar.getInstance()
        TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                viewModel.time = Date(
                    Calendar.getInstance()
                        .apply {
                            set(
                                viewModel.day!!.get(Calendar.YEAR),
                                viewModel.day!!.get(Calendar.MONTH),
                                viewModel.day!!.get(Calendar.DAY_OF_MONTH),
                                hourOfDay,
                                minute
                            )
                        }.timeInMillis
                )
            },
            now.get(Calendar.HOUR),
            now.get(Calendar.MINUTE),
            false
        ).show()
    }

    private fun createCourseWithInputs() {
        Timber.i("upload: create button clicked")
        viewModel.saveNewCourse()
    }

}