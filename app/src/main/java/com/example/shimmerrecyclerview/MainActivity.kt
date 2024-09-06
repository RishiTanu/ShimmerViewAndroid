package com.example.shimmerrecyclerview
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerViewWeather: RecyclerView
    private lateinit var btnToggleWeather: Button
    private lateinit var dialogWeather: LinearLayout

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var btnOpenDrawer: Button

    private lateinit var btnVisibility: Button
    private lateinit var btnWind: Button
    private lateinit var btnMetar: Button
    private lateinit var btnTaf: Button

    private var isDialogVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Setup RecyclerView
        btnToggleWeather = findViewById(R.id.btnToggleWeather)
        btnVisibility = findViewById(R.id.btn_visibility)
        btnWind = findViewById(R.id.btn_wind)
        btnMetar = findViewById(R.id.btn_metar)
        btnTaf = findViewById(R.id.btn_taf)


        setupToggleButton(btnVisibility) { isSelected ->
            Log.d("API", "Visibility data is ${if (isSelected) "enabled" else "disabled"}")

        }

        setupToggleButton(btnWind) { isSelected ->
            Log.d("API", "Wind data is ${if (isSelected) "enabled" else "disabled"}")
        }
        setupToggleButton(btnMetar) { isSelected ->
            Log.d("API", "Visibility data is ${if (isSelected) "enabled" else "disabled"}")

        }

        setupToggleButton(btnTaf) { isSelected ->
            Log.d("API", "Wind data is ${if (isSelected) "enabled" else "disabled"}")
        }


        // Toggle Weather Dialog
        btnToggleWeather.setOnClickListener {
            if (isDialogVisible) {
                hideWeatherDialog()
                Log.d("TAG", "onCreate: daaaaaaaaaaaaaaaaaaaa")
            } else {
                Log.d("TAG", "onCreate: dddddddddddddddddddddd")
                showWeatherDialog()
            }
        }


        // Initialize the drawer layout and button
        drawerLayout = findViewById(R.id.drawer_layout)
        btnOpenDrawer = findViewById(R.id.btn_open_drawer)

        // Open the drawer when the button is clicked
        btnOpenDrawer.setOnClickListener {
            if (!drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.openDrawer(GravityCompat.END)
            } else {
                drawerLayout.closeDrawer(GravityCompat.END)
            }
        }

        // Optionally handle closing the drawer when user swipes it back
        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                // Handle drawer sliding events if necessary
            }

            override fun onDrawerOpened(drawerView: View) {
                // Handle drawer opened event
            }

            override fun onDrawerClosed(drawerView: View) {
                // Handle drawer closed event
            }

            override fun onDrawerStateChanged(newState: Int) {
                // Handle drawer state changes if necessary
            }
        })
    }

    private fun showWeatherDialog() {
        // Start the animation from behind the toolbar
        val animate = TranslateAnimation(0f, 0f, -1050f, 0f)
        animate.duration = 500
        animate.fillAfter = true
        findViewById<View>(R.id.dialog_weather).visibility  = View.VISIBLE
        findViewById<View>(R.id.dialog_weather).startAnimation(animate)
        isDialogVisible = true
    }

    private fun hideWeatherDialog() {
        // Animate back to behind the toolbar
        val animate = TranslateAnimation(0f, 0f, 0f, -1050f)
        animate.duration = 500
        animate.fillAfter = true
        animate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                // Set visibility to gone after animation completes
                findViewById<View>(R.id.dialog_weather).visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
        findViewById<View>(R.id.dialog_weather).startAnimation(animate)
        isDialogVisible = false
    }

    private fun setupToggleButton(button: Button, action: (Boolean) -> Unit) {
        button.setOnClickListener {
            val isSelected = !button.isSelected
            button.isSelected = isSelected

            Log.d("TAG", "setupToggleButton: dddddeeeeeeeeeeeeeeeee")

            // Set background color based on selected state using button.context as the correct context
            if (isSelected) {
                // Set the selected color (e.g., blue)
                button.setBackgroundColor(ContextCompat.getColor(button.context, R.color.blue))
            } else {
                // Set the deselected color (e.g., grey)
                button.setBackgroundColor(ContextCompat.getColor(button.context, R.color.grey))
            }

            Log.d("ToggleButton", "${button.text} selected: $isSelected")
            action(isSelected)
        }
    }

}
