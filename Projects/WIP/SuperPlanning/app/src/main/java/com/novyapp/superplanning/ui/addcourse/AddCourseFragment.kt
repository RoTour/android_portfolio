package com.novyapp.superplanning.ui.addcourse

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.novyapp.superplanning.R
import com.novyapp.superplanning.data.Result
import com.novyapp.superplanning.databinding.AddCourseFragmentBinding
import timber.log.Timber
import java.text.DateFormat
import java.util.*


class AddCourseFragment : Fragment() {

//    companion object {
//        fun newInstance() = AddCourseFragment()
//    }

    private lateinit var binding: AddCourseFragmentBinding

    private val viewModel by viewModels<AddCourseViewModel> {
        AddCourseViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddCourseFragmentBinding.inflate(inflater, container, false)
        binding.loadingLayout.visibility = View.GONE

        setButtonsListeners()


        binding.datePickerButton.setOnClickListener { showDateTimePickers() }
        binding.createCourseButton.setOnClickListener { createCourseWithInputs() }

        viewModel.uploadResult.observe(viewLifecycleOwner) {
            if (it is Result.Loading) {
                binding.formLayout.visibility = View.GONE
                binding.loadingLayout.visibility = View.VISIBLE
            } else {
                binding.loadingLayout.visibility = View.GONE
                binding.formLayout.visibility = View.VISIBLE
                val msg = if (it is Result.Success) {
                    reset()
                    it.data
                } else (it as Result.Error).exception.message
                Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
            }
        }


        return binding.root
    }

    private fun setButtonsListeners() {
        binding.selectSubjectButton.setOnClickListener {
            createSelectDialog(DataTypes.SUBJECT.value) { newStr ->
                viewModel.subject(newStr)
                binding.selectSubjectButton.text = newStr
            }
        }
        binding.selectProfessorButton.setOnClickListener {
            createSelectDialog(DataTypes.PROFESSOR.value) { newStr ->
                viewModel.professor(newStr)
                binding.selectProfessorButton.text = newStr
            }
        }
        binding.selectPromotionButton.setOnClickListener {
            createSelectDialog(DataTypes.PROMOTION.value) { newStr ->
                viewModel.promotion(newStr)
                binding.selectPromotionButton.text = newStr
            }
        }
        binding.selectClassroomButton.setOnClickListener {
            createSelectDialog(DataTypes.CLASSROOM.value) { newStr ->
                viewModel.classroom(newStr)
                binding.selectClassroomButton.text = newStr
            }
        }
    }

    private fun createSelectDialog(dataType: String, onResult: (newValue: String) -> Unit) {
        activity?.let {
            val builder = AlertDialog.Builder(it)
            val arrayItems = viewModel.spinnersData.value?.get(dataType)?.toTypedArray()
                ?: arrayOf("+ Add $dataType")

            builder.setTitle("${R.string.empty_select} $dataType")
                .setItems(arrayItems) { _, which ->
                    if (which != 0) onResult(arrayItems[which])
                    else showNewFieldDialog(dataType, onResult)
                }
            builder.create().show()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun showNewFieldDialog(dataType: String, onResult: (newValue: String) -> Unit) {
        activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val dialogView = inflater.inflate(R.layout.add_value_dialog, null)

            builder.setView(dialogView)
                .setPositiveButton(R.string.confirm) { _, _ ->
                    val inputValue =
                        dialogView.findViewById<EditText>(R.id.newValue_editText)?.text.toString()
                    viewModel.newValueOn(dataType, inputValue)
                    onResult(inputValue)
                    Timber.i("spinner: $inputValue")
                }
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.cancel()
                }
            builder.create().show()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


    private fun reset() {
        viewModel.resetInputs()
        binding.selectSubjectButton.text = getString(R.string.select_subject_button)
        binding.selectProfessorButton.text = getString(R.string.select_professor_button)
        binding.selectPromotionButton.text = getString(R.string.select_promotion_button)
        binding.selectClassroomButton.text = getString(R.string.select_classroom_button)
        binding.datePreviewValueTextView.text = getString(R.string.not_selected)
        binding.timePreviewValueTextView.text = getString(R.string.not_selected)
    }


    private fun showDateTimePickers() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            requireContext(), { _, year1, monthOfYear, dayOfMonth ->
                // Creates a date with values provided from the Picker
                viewModel.day(Calendar.getInstance().apply { set(year1, monthOfYear, dayOfMonth) })

                showTimePicker()
                binding.datePreviewValueTextView.text =
                    DateFormat.getDateInstance(DateFormat.LONG)
                        .format(Date(viewModel.day().timeInMillis))
            }, year, month, day
        )

        dpd.show()
    }

    private fun showTimePicker() {
        val now = Calendar.getInstance()
        TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                viewModel.time(Date(Calendar.getInstance()
                            .apply {
                                set(
                                    viewModel.day().get(Calendar.YEAR),
                                    viewModel.day().get(Calendar.MONTH),
                                    viewModel.day().get(Calendar.DAY_OF_MONTH),
                                    hourOfDay,
                                    minute
                                )
                            }.timeInMillis))
                binding.timePreviewValueTextView.text =
                    DateFormat.getTimeInstance(DateFormat.SHORT).format(viewModel.time())
            },
            now.get(Calendar.HOUR),
            now.get(Calendar.MINUTE),
            false
        ).show()
    }

    private fun createCourseWithInputs() { viewModel.saveNewCourse() }

}