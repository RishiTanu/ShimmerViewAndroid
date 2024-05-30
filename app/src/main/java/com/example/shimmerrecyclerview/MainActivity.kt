package com.example.shimmerrecyclerview
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.arkapp.iosdatettimepicker.ui.DialogDateTimePicker
import com.arkapp.iosdatettimepicker.utils.OnDateTimeSelectedListener
import com.facebook.shimmer.ShimmerFrameLayout
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private val viewModel: YourViewModel by viewModels()


    private lateinit var shimmerLayout: ShimmerFrameLayout
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private var dataList = listOf<DummyItem>()

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

        val startDate: Calendar = Calendar.getInstance()
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

        // Custom method to enforce date constraints
    //    dateTimePickerDialog.setDateTimeRange(startDate.timeInMillis, endDate.timeInMillis)


        dateTimePickerDialog.show()

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


        // Observe network connectivity changes
        viewModel.getNetworkStatus().observe(this, Observer { isConnected ->
            if (isConnected) {
                // Make your API call here
                Toast.makeText(this, "Connected to the internet", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
            }
        })
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

    private fun loadData() {
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
    }
}