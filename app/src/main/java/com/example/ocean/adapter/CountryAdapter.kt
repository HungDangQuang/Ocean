package com.example.ocean.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.ocean.R

class CountryAdapter(private val countryList: Array<String>): RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_country, parent, false) as TextView

        return CountryViewHolder(textView)
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.textView.text = countryList[position]
    }

    inner class CountryViewHolder(val textView: TextView): ViewHolder(textView)

}