package com.novyapp.test.utlimatetodolist.ui.detail

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.novyapp.test.utlimatetodolist.R
import com.novyapp.test.utlimatetodolist.UTDLApplication
import com.novyapp.test.utlimatetodolist.databinding.TaskDetailFragmentBinding
import com.novyapp.test.utlimatetodolist.providers.ResourcesProvider


class TaskDetailFragment : Fragment() {

//    companion object {
//        fun newInstance() = TaskDetailFragment()
//    }

    private val viewModel by viewModels<TaskDetailViewModel> {
        TaskDetailViewModelFactory(
            (requireContext().applicationContext as UTDLApplication).repository
        )
    }
    private lateinit var binding: TaskDetailFragmentBinding
    private lateinit var args: TaskDetailFragmentArgs
    private lateinit var resProvider: ResourcesProvider


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TaskDetailFragmentBinding.inflate(inflater, container, false)
        args = TaskDetailFragmentArgs.fromBundle(arguments!!)
        resProvider = (requireContext().applicationContext as UTDLApplication).resourceProvider

        binding.taskTitleTextView.text = args.task.taskTitle
        binding.taskDescriptionTextView.text = args.task.taskText

        setHasOptionsMenu(true)

        binding.editTaskFab.setOnClickListener {
            this.findNavController().navigate(
                TaskDetailFragmentDirections.actionTaskDetailFragmentToAddOrEditFragment(args.task)
            )
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_options, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_delete -> {
                // This menu should not appear if args is null (ie if creating a new task)
                createConfirmDialog()
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }



    private fun createConfirmDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.delete_task_dialog_title)
        builder.setMessage(R.string.delete_task_dialog_message)

        builder.setPositiveButton(
            resProvider.getString(R.string.confirm)
        ) { _, _ ->
            viewModel.deleteThisTask(args.task.id)
            findNavController().navigateUp()
        }
        builder.setNegativeButton(resProvider.getString(R.string.cancel)) { _, _ ->
        }

        builder.show()
    }

}