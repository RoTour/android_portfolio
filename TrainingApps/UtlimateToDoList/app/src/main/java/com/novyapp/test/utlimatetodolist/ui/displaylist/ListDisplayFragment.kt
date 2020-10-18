package com.novyapp.test.utlimatetodolist.ui.displaylist

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.icu.util.DateInterval
import android.icu.util.TimeUnit
import android.os.Build
import android.os.Bundle
import android.text.format.DateUtils
import android.util.TimeUtils
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.novyapp.test.utlimatetodolist.R
import com.novyapp.test.utlimatetodolist.ReminderJobService
import com.novyapp.test.utlimatetodolist.UTDLApplication
import com.novyapp.test.utlimatetodolist.data.local.Task
import com.novyapp.test.utlimatetodolist.databinding.ListDiplayFragmentBinding
import timber.log.Timber
import java.util.*

private const val JOB_ID = 0

class ListDisplayFragment : Fragment() {

//    companion object {
//        fun newInstance() =
//            ListDisplayFragment()
//    }

    private val viewModel by viewModels<ListDisplayViewModel> {
        ListDisplayViewModelFactory(
            (requireContext().applicationContext as UTDLApplication).repository
        )
    }
    private lateinit var binding: ListDiplayFragmentBinding
    private var scheduler: JobScheduler? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = ListDiplayFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        val adapter = TaskListAdapter(
            TaskListListener { task: Task, _: TaskListAdapter.TaskViewHolder ->
                Timber.i("Clicked!! - id: ${task.id}, title: ${task.taskTitle}")
                this.findNavController()
                    .navigate(
                        ListDisplayFragmentDirections.actionListDisplayFragmentToTaskDetailFragment(
                            task
                        )
                    )
            },
            viewModel
        )

        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(this.activity)
        binding.tasksRecyclerView.adapter = adapter


        binding.newTaskFab.setOnClickListener {
            findNavController().navigate(ListDisplayFragmentDirections.actionListDisplayFragmentToAddOrEditFragment())
        }


//        viewModel.allTasks.observe(viewLifecycleOwner, Observer {
//            if(it != null && it is Result.Success){
//                Timber.i("VM all tasks is changed")
//            }
//        })

//        viewModel.eventUpdateUIIsNeeded.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                val tasks = viewModel.allTasks.value
//                if(tasks != null && tasks is Result.Success){
//                    adapter.submitList(tasks.data)
//                    Timber.i("Updating adapter list (updateUiObserver)")
//                }
//            }
//        })

        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.resetDB -> {
                viewModel.resetData()
                true
            }
            R.id.triggerJob -> {
                Toast.makeText(context, "Job scheduled", Toast.LENGTH_LONG).show()
                Timber.i("JOB STARTED")
                scheduleJob()
                true
            }
            R.id.filter_option -> {
                when (viewModel.toggleFilter()) {
                    TaskFilter.TODO -> item.icon = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_done_outline)
                    TaskFilter.DONE -> item.icon = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_done_all)
                    else -> item.icon = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_done)
                }
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }


    private fun scheduleJob() {
        scheduler =
            requireActivity().getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val jobService = ComponentName(
            requireContext().packageName, ReminderJobService::class.java.name
        )
        val jobInfoBuilder: JobInfo.Builder = JobInfo.Builder(JOB_ID, jobService)
        jobInfoBuilder.apply {
            setRequiresCharging(true)
            setRequiresDeviceIdle(true)
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) setRequiresBatteryNotLow(true)
            setPeriodic(1000L * 3600 * 24) // Send a notification every day
        }
        val jobInfo = jobInfoBuilder.build()
        scheduler!!.schedule(jobInfo)
        Toast.makeText(requireContext(), "Job scheduled", Toast.LENGTH_LONG).show()
    }

}