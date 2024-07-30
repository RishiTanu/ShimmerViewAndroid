package com.example.shimmerrecyclerview
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.arkapp.iosdatettimepicker.ui.DialogDateTimePicker
import com.arkapp.iosdatettimepicker.utils.OnDateTimeSelectedListener
import com.facebook.shimmer.ShimmerFrameLayout
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*shimmerLayout = findViewById(R.id.shimmer_layout)
        val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recycler_view)

        // Setup initial shimmer adapter
        recyclerViewAdapter = RecyclerViewAdapter(dataList, showShimmer = true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = recyclerViewAdapter

        // Simulate loading data with delay
        Handler(Looper.getMainLooper()).postDelayed({
            loadData()
        }, 3000)*/

       /* val startDate: Calendar = Calendar.getInstance()
        val endDate: Calendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, 27)
        }

        val dateTimeSelectedListener = object : OnDateTimeSelectedListener {
            override fun onDateTimeSelected(selectedDateTime: Calendar) {
                Toast.makeText(this@MainActivity, "Selected date: ${selectedDateTime.time}", Toast.LENGTH_LONG).show()
            }
        }

        val dateTimePickerDialog = DialogDateTimePicker(
            this, // context
            startDate, // start date
            1,
            dateTimeSelectedListener,
            "Select date and time" // dialog title
        )
        dateTimePickerDialog.show()*/

       /* dateTimePickerDialog.apply {
            setTitleTextColor(android.R.color.black)
            setDividerBgColor(android.R.color.black)
            setCancelBtnColor(R.color.grey)
            setCancelBtnTextColor(R.color.blue)
            setSubmitBtnColor(R.color.blue)
            setSubmitBtnTextColor(R.color.blue)
            setCancelBtnText("Dismiss")
            setSubmitBtnText("OK")
            setFontSize(14)
            setCenterDividerHeight(38)
        }*/

        val otp1: EditText = findViewById(R.id.otp1)
        val otp2: EditText = findViewById(R.id.otp2)
        val otp3: EditText = findViewById(R.id.otp3)
        val otp4: EditText = findViewById(R.id.otp4)

        otp1.addTextChangedListener(GenericTextWatcher(otp1, otp2))
        otp2.addTextChangedListener(GenericTextWatcher(otp2, otp3))
        otp3.addTextChangedListener(GenericTextWatcher(otp3, otp4))
        otp4.addTextChangedListener(GenericTextWatcher(otp4, null))

        otp1.setOnKeyListener(GenericKeyEvent(otp1, null))
        otp2.setOnKeyListener(GenericKeyEvent(otp2, otp1))
        otp3.setOnKeyListener(GenericKeyEvent(otp3, otp2))
        otp4.setOnKeyListener(GenericKeyEvent(otp4, otp3))
    }


    // Custom extension function to set date range
/*    fun DialogDateTimePicker.setDateTimeRange(minDate: Long, maxDate: Long) {
        val minDateCalendar = Calendar.getInstance().apply { timeInMillis = minDate }
        val maxDateCalendar = Calendar.getInstance().apply { timeInMillis = maxDate }

        *//*this.datePicker.minDate = minDateCalendar
        this.datePicker.maxDate = maxDateCalendar

        this.timePicker.minDate = minDateCalendar
        this.timePicker.maxDate = maxDateCalendar*//*
    }*/

   /* private fun loadData() {
        dataList = listOf(
            DummyItem("Title 1", "Description 1"),
            DummyItem("Title 2", "Description 2"),
            DummyItem("Title 3", "Description 3"),
            DummyItem("Title 4", "Description 4"),
            DummyItem("Title 5", "Description 5"),
            DummyItem("Title 5", "Description 5"),
            DummyItem("Title 5", "Description 5"),
            DummyItem("Title 5", "Description 5"),
            DummyItem("Title 5", "Description 5"),
        )

        recyclerViewAdapter = RecyclerViewAdapter(dataList, showShimmer = false)
        findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recycler_view).adapter = recyclerViewAdapter

        shimmerLayout.stopShimmer()
        shimmerLayout.visibility = android.view.View.VISIBLE
    }*/
   inner class GenericTextWatcher(private val currentEditText: EditText, private val nextEditText: EditText?) :
       TextWatcher {

       override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

       override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
           if (s?.length == 1) {
               nextEditText?.requestFocus()
           } else if (s?.length == 0) {
               currentEditText.requestFocus()
           }
       }

       override fun afterTextChanged(s: Editable?) {}
   }

    inner class GenericKeyEvent(private val currentEditText: EditText, private val previousEditText: EditText?) : View.OnKeyListener {

        override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
            if (event?.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                if (currentEditText.text.isEmpty() && previousEditText != null) {
                    previousEditText.text = null
                    previousEditText.requestFocus()
                    return true
                }
            }
            return false
        }
    }
}

