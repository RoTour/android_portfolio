package com.novyapp.superplanning.ui.main


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.novyapp.superplanning.R


class MySpinnerAdapter(
    context: Context,
    private var data: MutableList<String> = mutableListOf()
) : ArrayAdapter<String>(context, 0, data){



    fun submitList(newData: List<String>){
        data.clear()
        data.addAll(0, newData)
        data.add(0, context.getString(R.string.addSubject))
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): String {
        return data[position]
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

            view.findViewById<TextView>(R.id.item_textView).text = value

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