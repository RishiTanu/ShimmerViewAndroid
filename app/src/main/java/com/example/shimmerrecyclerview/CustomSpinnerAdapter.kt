package com.example.shimmerrecyclerview



// CustomSpinnerAdapter.kt
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.TextView

class CustomSpinnerAdapter(
    context: Context,
    private val items: Array<String>,
    private val onItemSelected: (position: Int) -> Unit
) : ArrayAdapter<String>(context, 0, items) {

    private var selectedPosition = -1

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false)
        val textView = view.findViewById<TextView>(R.id.textView)
        val radioButton = view.findViewById<RadioButton>(R.id.radioButton)
        radioButton.visibility = View.GONE
        textView.text = if (selectedPosition == -1) items[0] else items[selectedPosition]
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
            radioButton.isChecked = (position == selectedPosition)
            radioButton.setOnClickListener {
                selectedPosition = position
                notifyDataSetChanged()
                onItemSelected(position)
            }
        }

        return view
    }
}