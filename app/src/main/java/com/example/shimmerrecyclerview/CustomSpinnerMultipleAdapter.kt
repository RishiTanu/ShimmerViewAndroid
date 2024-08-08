package com.example.shimmerrecyclerview

// CustomSpinnerAdapter.kt
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.TextView


class CustomSpinnerMultipleAdapter(
    context: Context,
    private val items: Array<String>,
    private val onItemSelected: (selectedItems: List<String>) -> Unit
) : ArrayAdapter<String>(context, 0, items) {

    private val selectedPositions = mutableSetOf<Int>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false)
        val textView = view.findViewById<TextView>(R.id.textView)
        val radioButton = view.findViewById<RadioButton>(R.id.radioButton)
        radioButton.visibility = View.GONE

        textView.text = if (selectedPositions.isEmpty()) {
            items[0]
        } else {
            selectedPositions.joinToString(", ") { items[it] }
        }

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false)
        val textView = view.findViewById<TextView>(R.id.textView)
        val radioButton = view.findViewById<RadioButton>(R.id.radioButton)

        textView.text = items[position]

        if (position == 0) {
            radioButton.visibility = View.GONE
        } else {
            radioButton.visibility = View.VISIBLE
            radioButton.isChecked = selectedPositions.contains(position)

            radioButton.setOnClickListener {
                if (selectedPositions.contains(position)) {
                    selectedPositions.remove(position)
                } else {
                    selectedPositions.add(position)
                }
                notifyDataSetChanged()
                onItemSelected(selectedPositions.map { items[it] })
            }
        }

        return view
    }
}