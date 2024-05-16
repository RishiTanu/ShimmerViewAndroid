package com.example.shimmerrecyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CustomAutoCompleteAdapter(context: Context, private val results: List<SearchResult>) :
    ArrayAdapter<SearchResult>(context, 0, results) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_search_result, parent, false)
        val result = getItem(position)

        val textView1: TextView = view.findViewById(R.id.textView1)
        val textView2: TextView = view.findViewById(R.id.textView2)
        val textView3: TextView = view.findViewById(R.id.textView3)

        result?.let {
            textView1.text = it.value1
            textView2.text = it.value2
            textView3.text = it.value3
        }

        return view
    }
}
