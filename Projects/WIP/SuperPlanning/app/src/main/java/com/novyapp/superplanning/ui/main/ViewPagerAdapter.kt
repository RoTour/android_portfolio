package com.novyapp.superplanning.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.novyapp.superplanning.data.CourseV2
import com.novyapp.superplanning.databinding.PagerItemBinding
import timber.log.Timber

class ViewPagerAdapter : RecyclerView.Adapter<ViewPagerAdapter.WeekRecyclerViewViewHolder>() {

    /**
     * Map: WeekNumber to AssociatedCourseList
     */
    private var adapterList = hashMapOf<String, MutableList<CourseV2>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekRecyclerViewViewHolder {
        return WeekRecyclerViewViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: WeekRecyclerViewViewHolder, position: Int) {
        holder.bind(adapterList.values.toList()[position])
    }

    override fun getItemCount(): Int {
        return adapterList.size
    }

    fun submitList(data: HashMap<String, MutableList<CourseV2>>, forceClear: Boolean = false) {
//        Timber.i("newWeek: IS SUBMITTING")
//        Timber.i("newWeek: data: $data")

        if(forceClear) adapterList.clear()

        adapterList = data
//        data.forEach { (t, u) ->
//            Timber.i("newWeek: New Week: $t")
//            u.forEach {
//                Timber.i("newWeek: Course: ${it.subject}")
//            }
//        }
        notifyDataSetChanged()
    }

    class WeekRecyclerViewViewHolder private constructor(
            private val binding: PagerItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(coursesForOneWeek: MutableList<CourseV2>) {
            val oneWeekListAdapter = CourseListAdapter()
            oneWeekListAdapter.submitList(coursesForOneWeek)
            binding.recyclerView.adapter = oneWeekListAdapter
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): WeekRecyclerViewViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = PagerItemBinding.inflate(inflater, parent, false)
                return WeekRecyclerViewViewHolder(binding)
            }
        }
    }
}

