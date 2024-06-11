package com.example.shimmerrecyclerview

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.arkapp.iosdatettimepicker.ui.DialogDateTimePicker
import com.arkapp.iosdatettimepicker.utils.OnDateTimeSelectedListener
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class MainActivity2 : AppCompatActivity() {

    private lateinit var tvCurrentTime: TextView
    private val updateTimeJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + updateTimeJob)
    private lateinit var showDateTimePicker: Button
    private lateinit var selectedDateTv: TextView


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        tvCurrentTime = findViewById<TextView>(R.id.tv_current_time)
        println("GGGGGGGGGGGGG ${getCurrentDateTimeWithTimeZone()}");

        // Show the bottom sheet
        val bottomSheetFragment = YourBottomSheetFragment()
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)

        // Start updating the time every second
        coroutineScope.launch {
            while (isActive) {
                updateCurrentTime()
                delay(1000) // Update every second
            }
        }

      //  btnPickDate = findViewById(R.id.btn_pick_date)
        showDateTimePicker = findViewById<Button>(R.id.btn_pick_date_time)
        selectedDateTv = findViewById<TextView>(R.id.tv_selected_date_time)

        showDateTimePicker.setOnClickListener {
            showDateTimePicker()
        }

       /* showDateTimePicker.setOnClickListener {

            val startDate: Calendar = Calendar.getInstance()
            val endDate: Calendar = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, 27)
            }

            val dateTimeSelectedListener = object : OnDateTimeSelectedListener {
                override fun onDateTimeSelected(selectedDateTime: Calendar) {
                    Toast.makeText(
                        this@MainActivity2,
                        "Selected date ${selectedDateTime.time}",
                        Toast.LENGTH_SHORT
                    ).show()
                    selectedDateTv.text = selectedDateTime.time.toString()
                }
            }

            val dialog = DialogDateTimePicker(
                this,
                startDate,
                12,
                dateTimeSelectedListener,
                "Select date and time"
            )

            dialog.setTitleTextColor(com.arkapp.iosdatettimepicker.R.color.colorAccent)
            dialog.setDividerBgColor(R.color.blue)
            dialog.setCancelBtnColor(R.color.blue)
            dialog.setCancelBtnTextColor(R.color.blue)
            dialog.setSubmitBtnColor(R.color.blue)
            dialog.setSubmitBtnTextColor(R.color.grey)
            dialog.setCancelBtnText("Cancel")
            dialog.setSubmitBtnText("Submit")
            dialog.setFontSize(18)
            dialog.setCenterDividerHeight(48)
            dialog.show()
        }*/
    }


    private fun showDateTimePicker() {
        val minDate = Calendar.getInstance().time
        val maxDate = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 27)
        }.time

        SingleDateAndTimePickerDialog.Builder(this)
            .bottomSheet()
            .curved()
            .minutesStep(1)
            .minDateRange(minDate)
            .maxDateRange(maxDate)
            .title("Select Date and Time")
            .backgroundColor(resources.getColor(R.color.white, theme)) // Set the background color
            .mainColor(resources.getColor(com.arkapp.iosdatettimepicker.R.color.colorAccent, theme)) // Set the main color
            .titleTextColor(resources.getColor(R.color.grey, theme)) // Set the title text color
            .listener { date ->
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                val formattedDateTime = sdf.format(date)
                selectedDateTv.text = formattedDateTime
                Toast.makeText(this, "Selected date and time: $formattedDateTime", Toast.LENGTH_SHORT).show()
            }
            .display()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDateTimeWithTimeZone(): String {
        val currentDateTime = ZonedDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z")
        return currentDateTime.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateCurrentTime() {
        val currentDateTime = ZonedDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z")
        val formattedDateTime = currentDateTime.format(formatter)
        tvCurrentTime.text = formattedDateTime
    }

    override fun onDestroy() {
        super.onDestroy()
        updateTimeJob.cancel() // Cancel the job when the activity is destroyed
    }

}