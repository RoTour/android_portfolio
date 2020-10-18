package com.novyapp.dowittodolist.createtodo

import android.app.Application
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.novyapp.dowittodolist.R
import com.novyapp.dowittodolist.database.TodoDatabase
import com.novyapp.dowittodolist.databinding.NewTodoFragmentBinding
import com.novyapp.dowittodolist.toolbar

import com.novyapp.dowittodolist.utils.longToFormattedDate
import java.util.*

class CreateTodoFragment : Fragment() {

    lateinit var binding: NewTodoFragmentBinding
    lateinit var application: Application

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NewTodoFragmentBinding.inflate(inflater, container, false)
        application = requireNotNull(this.activity).application
        val database = TodoDatabase.getInstance(application).todoDatabaseDao
        val viewModelFactory = CreateTodoViewModelFactory(database, application)
        val viewModel = ViewModelProviders
            .of(this, viewModelFactory).get(CreateTodoViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        setFragmentObservers(viewModel)



        toolbar.title = "Todo Creator"
        return binding.root
    }

    private fun setFragmentObservers(viewModel: CreateTodoViewModel) {
        setTaskEditTextListener(viewModel)
        setPickupDateListener(viewModel)
        setPickupDateLabelListener(viewModel)
        setCreateTodoListener(viewModel)
        setErrorFieldMissingListener(viewModel)
        setNavigateToMainListener(viewModel)
    }

    private fun setTaskEditTextListener(viewModel: CreateTodoViewModel) {
        binding.taskEditText.text = Editable.Factory.getInstance().newEditable(viewModel.todoTask.value)
        binding.taskEditText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                viewModel.todoTaskUpdated(s)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun setPickupDateListener(viewModel: CreateTodoViewModel) {
        viewModel.eventPickupDateButtonPressed.observe(viewLifecycleOwner, Observer {
            if (it) {
                val c = Calendar.getInstance()
                val cYear = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)
                val dpd = DatePickerDialog(
                    context!!,
                    DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                        viewModel.newDateSelected(dayOfMonth, monthOfYear, year)
                    }, cYear, month, day
                )
                dpd.show()
                viewModel.onPickupDateButtonClickHandled()
            }
        })
    }

    private fun setPickupDateLabelListener(viewModel: CreateTodoViewModel) {
        viewModel.dueDate.observe(this, Observer {
            binding.dueDateText.text = longToFormattedDate(viewModel.dueDate.value!!)
        })
    }

    private fun setCreateTodoListener(viewModel: CreateTodoViewModel) {
        viewModel.eventCreateTodoButtonPressed.observe(viewLifecycleOwner, Observer {
            if(it){
                viewModel.pushTodoInDatabase()
                viewModel.onCreateTodoButtonClickHandled()
            }
        })
    }

    private fun setErrorFieldMissingListener(viewModel: CreateTodoViewModel) {
        viewModel.eventErrorFieldMissing.observe(this, Observer {
            if(it){
                Snackbar.make(
                    binding.root,
                    application.getString(R.string.error_missing_values),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun setNavigateToMainListener(viewModel: CreateTodoViewModel) {
        viewModel.eventNavigateToMain.observe(this, Observer {
            if(it){
                findNavController().navigate(CreateTodoFragmentDirections.actionCreateTodoFragmentToMainFragment())
            }
        })
    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        val item = menu.findItem(R.id.addTodo)
        val item2 = menu.findItem(R.id.about)
        item?.isVisible = false
        item2.isVisible = false
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


}