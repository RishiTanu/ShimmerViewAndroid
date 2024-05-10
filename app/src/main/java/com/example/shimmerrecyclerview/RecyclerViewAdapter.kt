package com.example.shimmerrecyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(
    private val dataList: List<DummyItem>,
    private val showShimmer: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val shimmerItemCount = 10

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (showShimmer) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_shimmer_placeholder, parent, false)
            ShimmerViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_normal, parent, false)
            NormalViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return if (showShimmer) shimmerItemCount else dataList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NormalViewHolder && !showShimmer) {
            val item = dataList[position]
            holder.title.text = item.title
            holder.description.text = item.description
        }
    }

    inner class NormalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val description: TextView = view.findViewById(R.id.description)
    }

    inner class ShimmerViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
