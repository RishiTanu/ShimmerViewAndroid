package com.example.shimmerrecyclerview

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Filter : AppCompatActivity() {

    private lateinit var filterDialog: LinearLayout
    private var isDialogVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        // Access resources or views after this point
        // Initialize the dialog layout and button
        val btnFilter = findViewById<View>(R.id.btnFilter)
        filterDialog = findViewById(R.id.filterDialog)


        // Toggle filter dialog visibility on button click
        btnFilter.setOnClickListener {
            if (isDialogVisible) {
                hideFilterDialog()
            } else {
                showFilterDialog()
            }
        }
    }



    private fun showFilterDialog() {
        filterDialog.visibility = View.VISIBLE

        // Start the animation from behind the toolbar
        val animate = TranslateAnimation(0f, 0f, -1050f, 0f)
        animate.duration = 500
        animate.fillAfter = true
        filterDialog.startAnimation(animate)

        isDialogVisible = true
    }

    private fun hideFilterDialog() {
        // Animate back to behind the toolbar
        val animate = TranslateAnimation(0f, 0f, 0f, -1050f)
        animate.duration = 500
        animate.fillAfter = true
        filterDialog.startAnimation(animate)

        // Set visibility to gone after animation completes
        animate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                filterDialog.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })

        isDialogVisible = false
    }

}