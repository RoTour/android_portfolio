package com.novyapp.superplanning.ui.addcourse

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import com.novyapp.superplanning.R
import com.novyapp.superplanning.data.Result
import com.novyapp.superplanning.databinding.AddCourseFragmentBinding
import com.novyapp.superplanning.ui.main.MySpinnerAdapter
import timber.log.Timber
import java.text.DateFormat
import java.util.*
import kotlin.collections.LinkedHashMap


class AddCourseFragment : Fragment() {

//    companion object {
//        fun newInstance() = AddCourseFragment()
//    }

    private lateinit var binding: AddCourseFragmentBinding
    private var inputValues = LinkedHashMap<Button, String>()
    lateinit var subjectSpinnerAdapter: MySpinnerAdapter

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

        inputValues[binding.selectSubjectButton] = viewModel.subject

        subjectSpinnerAdapter = MySpinnerAdapter(requireContext())

        setInputsListeners()
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
        inputValues.forEach { (key, value) ->
            key.setOnClickListener { buttonView ->
                activity?.let {
                    val builder = AlertDialog.Builder(it)
                    val items: List<*> = (viewModel.spinnersData.value?.get("Subjects") as List<*>)
                    val arrayItems: Array<String> = items.filterIsInstance<String>().toTypedArray()

                    builder.setTitle(R.string.select_subject_button)
                        .setItems(
                            arrayItems,
                            DialogInterface.OnClickListener { dialog, which ->
                                // The 'which' argument contains the index position
                                // of the selected item
                            })
                    builder.create().show()
                } ?: throw IllegalStateException("Activity cannot be null")
            }
        }
    }


    private fun reset() {
        viewModel.resetInputs()
//        binding.subjectEditText.text = Editable.Factory().newEditable("")
        binding.classroomEditText.text = Editable.Factory().newEditable("")
        binding.promotionEditText.text = Editable.Factory().newEditable("")
        binding.professorEditText.text = Editable.Factory().newEditable("")
    }


    // Ugly code
    private fun setInputsListeners() {
//

//        viewModel.subject = "Javascript"

        binding.classroomEditText.addTextChangedListener {
            it?.let { viewModel.classroom = it.toString() } ?: run { viewModel.classroom = "" }
        }
        binding.promotionEditText.addTextChangedListener {
            it?.let { viewModel.promotion = it.toString() } ?: run { viewModel.promotion = "" }
        }
        binding.professorEditText.addTextChangedListener {
            it?.let { viewModel.professor = it.toString() } ?: run { viewModel.professor = "" }
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
                binding.datePreviewValueTextView.text =
                    DateFormat.getDateInstance(DateFormat.LONG)
                        .format(Date(viewModel.day!!.timeInMillis))
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
                binding.timePreviewValueTextView.text =
//                    android.text.format.DateFormat.format("hh:mm", viewModel.time!!)
                    DateFormat.getTimeInstance(DateFormat.SHORT).format(viewModel.time!!)
            },
            now.get(Calendar.HOUR),
            now.get(Calendar.MINUTE),
            false
        ).show()
    }

    private fun createCourseWithInputs() {
        viewModel.checkInputsEmpty()
        Timber.i("upload: create button clicked")
        viewModel.saveNewCourse()
    }

}