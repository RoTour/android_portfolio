package com.novyapp.test.utlimatetodolist.ui.testviewpager

import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.novyapp.test.utlimatetodolist.R
import com.novyapp.test.utlimatetodolist.data.local.Task
import com.novyapp.test.utlimatetodolist.ui.displaylist.TaskFilter
import kotlinx.android.synthetic.main.item_pager.view.*
import timber.log.Timber

class ViewPagerAdapter(
    val clickListener: (item: View, selectedText: String, task: Task) -> Unit
) : RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>() {

    private val filters = TaskFilter.values()
    private var currentList: List<Task> = emptyList()

    inner class ViewPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    fun submitList(list: List<Task>) {
        currentList = list
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pager, parent, false)
        return ViewPagerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filters.size
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val filter: TaskFilter = filters[position]
        val filteredList: LinkedHashMap<String, Task> = linkedMapOf()

        filter(currentList, filter).associateByTo(filteredList) { it.taskTitle }

        val radioGroup: RadioGroup = holder.itemView.findViewById<RadioGroup>(R.id.radioGroup_pager)
        filteredList.forEach {
            Timber.i("Adapter: filter: ${filter.name} task: ${it.key}")
        }
        filteredList.forEach {
            val newButton = RadioButton(holder.itemView.context)
            newButton.text = it.key
            radioGroup.addView(newButton)
        }

        holder.itemView.confirm_button_pager.setOnClickListener {view ->
            val taskTextSelected: String? =
                holder.itemView.findViewById<RadioButton>(
                    holder.itemView.radioGroup_pager.checkedRadioButtonId
                ).text.toString()
            taskTextSelected?.let {
                clickListener(
                    view,
                    it,
                    filteredList[taskTextSelected]!!
                )
            }
        }
    }

    private fun filter(currentList: List<Task>, filter: TaskFilter): List<Task> {
        Timber.i("Adapter: FILTERING")
        val result = ArrayList<Task>()
        for (task in currentList) {
            when (filter) {
                TaskFilter.TODO -> {
                    if (!task.isCompleted) result.add(task)
                }
                TaskFilter.DONE -> {
                    if (task.isCompleted) result.add(task)
                }
                else -> {
                    result.add(task)
                }
            }
        }

        return result
    }

    class MyClickListener(val action: (item: View) -> Unit) {
        fun clickListener(item: View) = action(item)
    }

}
