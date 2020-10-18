package com.novyapp.dowittodolist.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.novyapp.dowittodolist.R
import com.novyapp.dowittodolist.database.TodoDatabase
import com.novyapp.dowittodolist.databinding.MainFragmentBinding
import com.novyapp.dowittodolist.toolbar


class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = MainFragmentBinding.inflate(inflater, container, false)

        val application = requireNotNull(this.activity).application
        val database = TodoDatabase.getInstance(application).todoDatabaseDao
        val viewModelFactory = MainVIewModelFactory(database, application)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)



        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.appname.visibility = View.GONE

        val adapter =
            TodosAdapter( TodoClickListener { id: Long, viewHolder: TodosAdapter.TodoViewHolder ->
                viewHolder.handleRemoveAnimation(context){
                    viewModel.onRemoveAnimationFinished(id, viewHolder.itemView)
                }
            val cancelSnack: Snackbar =
                Snackbar.make(binding.fragmentLayout, "Task Done!", Snackbar.LENGTH_LONG)
                    .setAction(R.string.cancel){
                        viewModel.restoreDeletedTodo()
                    }
                cancelSnack.show()

        })

        binding.todolistRecyclerview.layoutManager = LinearLayoutManager(this.activity)
        binding.todolistRecyclerview.adapter = adapter

        viewModel.todos.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        toolbar.title = "Your tasks"
        return binding.root
    }

}