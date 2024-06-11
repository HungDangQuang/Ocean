package com.example.ocean.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ocean.databinding.MenuItemBinding

class MenuItemAdapter(private val itemList: List<MenuItem>, private val onMenuItemClickListener: OnMenuItemClickListener) :
    RecyclerView.Adapter<MenuItemAdapter.ViewHolder>() {

    class ViewHolder(private val itemBinding: MenuItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(item: MenuItem, listener: OnMenuItemClickListener) {
            itemBinding.menuItem = item
            itemBinding.executePendingBindings()
            itemView.setOnClickListener {
                listener.onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MenuItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item, onMenuItemClickListener)
    }

}

interface OnMenuItemClickListener {
    fun onItemClick(item: MenuItem)
}
