package com.example.shimmerrecyclerview

import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class MainActivity2 : AppCompatActivity() {

    private lateinit var showDateTimePicker: Button

    private lateinit var tvSelectedTime: TextView

    private lateinit var tvStatus: TextView
    private lateinit var progressBar: ProgressBar

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)


        showDateTimePicker = findViewById<Button>(R.id.btnSelectTime)
        tvSelectedTime = findViewById<TextView>(R.id.tvSelectedTime)

        showDateTimePicker.setOnClickListener {
            showTimePickerDialog()
        }
        tvStatus = findViewById<Button>(R.id.tvStatus)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)

        // Show the progress bar and status text
        tvStatus.visibility = android.view.View.VISIBLE
        progressBar.visibility = android.view.View.VISIBLE

        // Use a Handler to dismiss the progress bar after 5 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            tvStatus.visibility = android.view.View.GONE
            progressBar.visibility = android.view.View.GONE
        }, 5000)



        // Initialize the switch and text view
        val switchFlightPlan: SwitchMaterial = findViewById(R.id.switchFlightPlan)
        val tvFlightPlanStatus: TextView = findViewById(R.id.tvFlightPlanStatus)

        // Set up the switch listener
        switchFlightPlan.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tvFlightPlanStatus.text = "Active"
                tvFlightPlanStatus.setTextColor(resources.getColor(android.R.color.holo_green_dark))
            } else {
                tvFlightPlanStatus.text = "Deactive"
                tvFlightPlanStatus.setTextColor(resources.getColor(android.R.color.holo_red_dark))
            }
        }

    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        // Initialize the MaterialTimePicker
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(currentHour)
            .setMinute(currentMinute)
            .setTitleText("Select Appointment time")
            .build()

        picker.addOnPositiveButtonClickListener {
            val selectedHour = picker.hour
            val selectedMinute = picker.minute

            // Get current time and maximum valid time
            val now = Calendar.getInstance()
            val maxTime = Calendar.getInstance().apply {
                add(Calendar.MINUTE, 30)
            }

            // Create Calendar instance for the selected time
            val selectedTimeCalendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, selectedHour)
                set(Calendar.MINUTE, selectedMinute)
            }

            // Validate the selected time
            if (selectedTimeCalendar.before(now) || selectedTimeCalendar.after(maxTime)) {
                Toast.makeText(this, "Please select a time within the next 30 minutes.", Toast.LENGTH_SHORT).show()
                showTimePickerDialog()  // Re-open the TimePicker dialog
            } else {
                val selectedIsPM = selectedHour >= 12
                val formattedTime = String.format(
                    "%02d:%02d %s",
                    if (selectedHour > 12) selectedHour - 12 else selectedHour,
                    selectedMinute,
                    if (selectedIsPM) "PM" else "AM"
                )
                tvSelectedTime.text = "Selected Time: $formattedTime"
            }
        }

        picker.show(supportFragmentManager, "MATERIAL_TIME_PICKER")
    }
}