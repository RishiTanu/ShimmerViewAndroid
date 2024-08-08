package com.example.shimmerrecyclerview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.ViewCompat


class CustomSpinnerWithEditTextAdapter(
    context: Context,
    private val items: Array<String>
) : ArrayAdapter<String>(context, 0, items) {

    private val valuesMap = mutableMapOf<String, String>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_item_with_edittext, parent, false)
        val textView = view.findViewById<TextView>(R.id.textView)
        val editText = view.findViewById<EditText>(R.id.editText)

        textView.text = if (position == 0) items[0] else items[position]
        editText.visibility = View.GONE

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_item_with_edittext, parent, false)
        val textView = view.findViewById<TextView>(R.id.textView)
        val editText = view.findViewById<EditText>(R.id.editText)

        textView.text = items[position]

        if (position == 0) {
            editText.visibility = View.GONE
        } else {
            editText.visibility = View.VISIBLE
            editText.setText(valuesMap[items[position]] ?: "")

            editText.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    ViewCompat.postOnAnimationDelayed(v, {
                        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
                        imm.showSoftInput(v, android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT)
                    }, 100)
                }
            }

            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    valuesMap[items[position]] = s.toString()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            editText.setOnTouchListener { v, event ->
                v.parent.requestDisallowInterceptTouchEvent(true)
                false
            }
        }

        return view
    }

    fun getValuesMap(): Map<String, String> {
        return valuesMap
    }
}