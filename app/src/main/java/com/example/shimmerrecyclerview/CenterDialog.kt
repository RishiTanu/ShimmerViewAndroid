package com.example.shimmerrecyclerview

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CenterDialog : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_center_dialog)

        val btnOpenDialog = findViewById<Button>(R.id.btnOpenDialog)

        btnOpenDialog.setOnClickListener {
            // Create an instance of MyDialogFragment and show it
            val dialogFragment = MyDialogFragment()
            dialogFragment.show(supportFragmentManager, "MyDialogFragment")
        }
    }
}