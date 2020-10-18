package com.novyapp.test.utlimatetodolist.ui.testviewpager

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.novyapp.test.utlimatetodolist.R
import com.novyapp.test.utlimatetodolist.UTDLApplication
import com.novyapp.test.utlimatetodolist.data.Result
import com.novyapp.test.utlimatetodolist.data.local.Task
import com.novyapp.test.utlimatetodolist.databinding.TestFragmentBinding
import kotlinx.android.synthetic.main.item_pager.view.*
import kotlinx.android.synthetic.main.test_fragment.*

class TestViewPagerFragment : Fragment() {

    companion object {
        fun newInstance() =
            TestViewPagerFragment()
    }

    private lateinit var binding: TestFragmentBinding
    private val viewModel by viewModels<TestViewPagerViewModel> {
        TestViewPagerViewModelFactory(
            (requireContext().applicationContext as UTDLApplication).repository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TestFragmentBinding.inflate(inflater, container, false)

        val adapter = ViewPagerAdapter() { _, buttonText, task ->
            Toast.makeText(context, buttonText, Toast.LENGTH_LONG).show()
            findNavController().navigate(TestViewPagerFragmentDirections.actionTestViewPagerFragmentToTaskDetailFragment(task))
        }
        binding.viewPager.adapter = adapter
        var radioSelected: RadioButton?

        viewModel.allTasks.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it is Result.Success) adapter.submitList(it.data)
            }
        })



        return binding.root
    }

}