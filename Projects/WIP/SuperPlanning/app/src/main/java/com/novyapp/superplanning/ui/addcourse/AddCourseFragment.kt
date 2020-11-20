package com.novyapp.superplanning.ui.addcourse

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.novyapp.superplanning.databinding.AddCourseFragmentBinding
import timber.log.Timber
import java.text.DateFormat
import java.util.*

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

        setInputsListeners()

        binding.datePickerButton.setOnClickListener { showDatePicker() }

        binding.createCourseButton.setOnClickListener { createCourseWithInputs() }


        return binding.root
    }

    private fun setInputsListeners() {
        binding.subjectEditText.addTextChangedListener {
            if (it != null) viewModel.subject = it.toString()
            else viewModel.subject = ""
        }
        binding.classroomEditText.addTextChangedListener {
            if (it != null) viewModel.classroom = it.toString()
            else viewModel.classroom = ""
        }
        binding.professorEditText.addTextChangedListener {
            if (it != null) viewModel.professor = it.toString()
            else viewModel.professor = ""
        }
        binding.promotionEditText.addTextChangedListener {
            if (it != null) viewModel.promotion = it.toString()
            else viewModel.promotion = ""
        }
    }

    private fun showDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
                requireContext(), { _, year1, monthOfYear, dayOfMonth ->

            // Display Selected date in textbox
            Toast.makeText(
                    requireContext(),
                    "" + dayOfMonth + " " + (monthOfYear + 1) + ", " + year1,
                    Toast.LENGTH_LONG
            ).show()
            showTimePicker()
        }, year, month, day)

        dpd.show()
    }

    private fun showTimePicker() {
        val nowCalendar = Calendar.getInstance()
        TimePickerDialog(
                requireContext(),
                { _, hourOfDay, minute ->
                    Toast.makeText(
                            requireContext(),
                            "$hourOfDay:$minute",
                            Toast.LENGTH_LONG
                    ).show()
                },
                nowCalendar.get(Calendar.HOUR),
                nowCalendar.get(Calendar.MINUTE),
                false
        ).show()
    }

    private fun createCourseWithInputs() {

    }

}