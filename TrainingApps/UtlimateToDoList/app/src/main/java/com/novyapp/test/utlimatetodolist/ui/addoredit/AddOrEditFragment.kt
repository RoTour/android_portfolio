package com.novyapp.test.utlimatetodolist.ui.addoredit

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.novyapp.test.utlimatetodolist.R
import com.novyapp.test.utlimatetodolist.providers.ResourcesProvider
import com.novyapp.test.utlimatetodolist.UTDLApplication
import com.novyapp.test.utlimatetodolist.data.local.Task
import com.novyapp.test.utlimatetodolist.databinding.AddOrEditFragmentBinding
import timber.log.Timber

class AddOrEditFragment : Fragment() {

    companion object {
        fun newInstance() = AddOrEditFragment()
    }

    private val viewModel by viewModels<AddOrEditViewModel> {
        AddOrEditViewModelFactory(
            (requireContext().applicationContext as UTDLApplication).repository
        )
    }

    private lateinit var binding: AddOrEditFragmentBinding
    private lateinit var args: AddOrEditFragmentArgs
    private lateinit var resProvider: ResourcesProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddOrEditFragmentBinding.inflate(layoutInflater, container, false)
        args = AddOrEditFragmentArgs.fromBundle(arguments!!)
        resProvider = (requireContext().applicationContext as UTDLApplication).resourceProvider


        args.task?.let {
            binding.taskTitleEditText.setText(it.taskTitle)
            binding.taskDescriptionTextView.setText(it.taskText)
            setHasOptionsMenu(true)
        } ?: run {
            binding.taskTitleEditText.hint = resProvider.getString(R.string.task_title_hint)
            binding.taskDescriptionTextView.hint =
                resProvider.getString(R.string.task_description_hint)
        }

        binding.validateTaskFab.setOnClickListener {
            Timber.i("title is: ${binding.taskTitleEditText.text}\n and desc is ${binding.taskDescriptionTextView.text}")
            args.task?.let {
                val updatedTask = Task(
                    it.id,
                    binding.taskTitleEditText.text.toString(),
                    binding.taskDescriptionTextView.text.toString()
                )
                viewModel.updateTask(updatedTask)
                findNavController().navigate(
                    AddOrEditFragmentDirections.actionAddOrEditFragmentToTaskDetailFragment(
                        updatedTask
                    )
                )

            } ?: run {
                if (binding.taskTitleEditText.text.toString() == "") {
                    Toast.makeText(
                        requireContext(),
                        resProvider.getString(R.string.titleIsEmpty),
                        Toast.LENGTH_LONG
                    ).show()
                    return@run
                }
                val newTask = Task(
                    taskTitle = binding.taskTitleEditText.text.toString(),
                    taskText = binding.taskDescriptionTextView.text.toString()
                )
                viewModel.saveNewTask(newTask)
                findNavController().navigate(
                    AddOrEditFragmentDirections.actionAddOrEditFragmentToListDisplayFragment()
                )
            }
        }




        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_options, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_delete -> {
                // This menu should not appear if args is null (ie if creating a new task)
                viewModel.deleteThisTask(args.task!!.id)
                findNavController().navigate(AddOrEditFragmentDirections.actionAddOrEditFragmentToListDisplayFragment())
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }
}