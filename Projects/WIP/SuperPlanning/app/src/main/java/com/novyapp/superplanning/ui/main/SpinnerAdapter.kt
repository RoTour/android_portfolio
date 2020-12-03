package com.novyapp.superplanning.ui.main


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.SpinnerAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.novyapp.superplanning.R
import com.novyapp.superplanning.databinding.CustomSpinnerItemBinding


class MySpinnerAdapter(
    context: Context,
    private var data: MutableList<String> = mutableListOf()
) : ArrayAdapter<String>(context, 0, data){

    init {
        setNotifyOnChange(true)
    }

    fun submitList(data: List<String>){
        this.data.clear()
        this.data.addAll(0, data)
        this.notifyDataSetChanged()
    }



    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    private fun createItemView(position: Int, recycledView: View?, parent: ViewGroup):View {
        val value = getItem(position)

        val view = recycledView ?: LayoutInflater.from(context).inflate(
            R.layout.custom_spinner_item,
            parent,
            false
        )

        value?.let {
            view.findViewById<TextView>(R.id.item_textView).text = value
        }
        return view
    }
}


//class SpinnerAdapter : ListAdapter<String, SpinnerAdapter.ViewHolder>(SpinnerCallback()) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return ViewHolder.fromParent(parent)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(currentList[position])
//    }
//
//    class ViewHolder private constructor(
//        private val binding: CustomSpinnerItemBinding
//    ) : RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(item: String) {
//            binding.itemTextView.text = item
//            binding.executePendingBindings()
//        }
//
//        companion object {
//            fun fromParent(parent: ViewGroup) : ViewHolder{
//                val inflater = LayoutInflater.from(parent.context)
//                val binding = CustomSpinnerItemBinding.inflate(inflater, parent, false)
//                return ViewHolder(binding)
//            }
//        }
//
//    }
//}
//
//class SpinnerCallback : DiffUtil.ItemCallback<String>(){
//    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
//        return oldItem == newItem
//    }
//
//    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
//        return oldItem == newItem
//    }
//
//}