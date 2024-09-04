package com.example.shimmerrecyclerview
import android.os.Bundle
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerViewWeather: RecyclerView
    private lateinit var btnToggleWeather: Button
    private lateinit var dialogWeather: LinearLayout

    private var isDialogVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        recyclerViewWeather = findViewById(R.id.recyclerViewWeather)
        btnToggleWeather = findViewById(R.id.btnToggleWeather)
        dialogWeather = findViewById(R.id.dialogWeather)

        // Setup RecyclerView
        val dummyItems = listOf("Temperature", "Wind", "Rain Report", "Forecast")
        val adapter = DummyAdapter(dummyItems)
        recyclerViewWeather.layoutManager = LinearLayoutManager(this)
        recyclerViewWeather.adapter = adapter

        // Toggle Weather Dialog
        btnToggleWeather.setOnClickListener {
            if (isDialogVisible) {
                hideWeatherDialog()
            } else {
                showWeatherDialog()
            }
        }
    }

    private fun showWeatherDialog() {
        dialogWeather.visibility = View.VISIBLE
        val animate = TranslateAnimation(0f, 0f, dialogWeather.height.toFloat(), 0f)
        animate.duration = 500
        animate.fillAfter = true
        dialogWeather.startAnimation(animate)

        // Update button text
        btnToggleWeather.text = "Weather ⬆"
        isDialogVisible = true
    }

    private fun hideWeatherDialog() {
        val animate = TranslateAnimation(0f, 0f, 0f, dialogWeather.height.toFloat())
        animate.duration = 500
        animate.fillAfter = true
        dialogWeather.startAnimation(animate)
        dialogWeather.visibility = View.GONE

        // Update button text
        btnToggleWeather.text = "Weather ⬇"
        isDialogVisible = false
    }
}
