package com.example.ocean.ui.adapter

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.domain.model.Country
import com.example.ocean.R
import com.example.ocean.databinding.ItemCountryBinding
import com.example.ocean.databinding.ItemLoadingBinding

class CountryAdapter(
    private val countryList: MutableList<Country>,
    private val context: Context
) : RecyclerView.Adapter<ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
        private val TAG = CountryAdapter::class.java.simpleName
        private var selectedItemPosition: Int = RecyclerView.NO_POSITION
    }

    inner class CountryViewHolder(val itemBinding: ItemCountryBinding) :
        ViewHolder(itemBinding.root) {
        init {
            itemBinding.clItem.setOnClickListener {
                Log.d(TAG, "recyclerview item ${itemBinding.tvInputLanguageCountry.text} is clicked")
                notifyItemChanged(selectedItemPosition) // Reset previous selected item
                selectedItemPosition = adapterPosition
                notifyItemChanged(selectedItemPosition)
            }
        }

        fun bind(text: String, bitmap: Bitmap) {
            itemBinding.tvInputLanguageCountry.text = text
            itemBinding.imageFlag.setImageBitmap(bitmap)
        }
    }

    inner class LoadingViewHolder(itemBinding: ItemLoadingBinding) : ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_ITEM) {
            val binding = ItemCountryBinding.inflate(inflater, parent, false)
            CountryViewHolder(binding)
        } else {
            val binding = ItemLoadingBinding.inflate(inflater, parent, false)
            LoadingViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    override fun getItemId(position: Int): Long {
        return countryList[position].hashCode().toLong()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is CountryViewHolder) {
            // show item
            val item = countryList[position]
            if (position == selectedItemPosition) {
                holder.itemBinding.clItem.background = ContextCompat.getDrawable(
                    holder.itemBinding.clItem.context,
                    R.drawable.bg_ripple_item
                )
            } else {
                holder.itemBinding.clItem.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.background_gray
                    )
                ) // Default background
            }
            holder.bind(item.countryName, item.countryFlag)
        } else {
            // show loading bar
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (countryList[position].countryName.isBlank()) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    fun addItems(newItems: List<Country>) {
        val startPosition = countryList.size
        countryList.addAll(newItems)
        notifyItemRangeInserted(startPosition, newItems.size)
    }

    fun showLoading() {
        Log.d(TAG, "showLoading")
        if (countryList.lastOrNull()?.countryName?.isNotBlank() == true || countryList.isEmpty()) {
            countryList.add(
                Country(
                    Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888),
                    ""
                )
            ) // Add a loading item
            notifyItemInserted(countryList.size - 1)
        }
    }

    fun hideLoading() {
        Log.d(TAG, "hideLoading")
        if (countryList.lastOrNull()?.countryName?.isBlank() == true) {
            val position = countryList.size - 1
            countryList.removeAt(position) // Remove the loading item
            notifyItemRemoved(position)
        }
    }

    fun getSelectedItem() : Country {
        Log.d(TAG, "getSelectingItem")
        return countryList[selectedItemPosition]
    }

}   