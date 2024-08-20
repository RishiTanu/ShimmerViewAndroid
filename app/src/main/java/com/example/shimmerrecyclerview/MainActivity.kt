package com.example.shimmerrecyclerview
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.clans.fab.FloatingActionButton
import com.github.clans.fab.FloatingActionMenu

class MainActivity : AppCompatActivity() {
    private lateinit var fabMenu: FloatingActionMenu
    private lateinit var fabOption1: FloatingActionButton
    private lateinit var fabOption2: FloatingActionButton
    private lateinit var fabOption3: FloatingActionButton
    private lateinit var fabOption4: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        fabMenu = findViewById(R.id.fabMenu)
        fabOption1 = findViewById(R.id.fabOption1)
        fabOption2 = findViewById(R.id.fabOption2)
        fabOption3 = findViewById(R.id.fabOption3)
        fabOption4 = findViewById(R.id.fabOption4)

    }

    override fun onResume() {
        super.onResume()
        fabOption1.setOnClickListener {
            Toast.makeText(this, "Option 1 clicked", Toast.LENGTH_SHORT).show()
        }

        fabOption2.setOnClickListener {
            Toast.makeText(this, "Option 2 clicked", Toast.LENGTH_SHORT).show()
        }

        fabOption3.setOnClickListener {
            Toast.makeText(this, "Option 3 clicked", Toast.LENGTH_SHORT).show()
        }

        fabOption4.setOnClickListener {
            Toast.makeText(this, "Option 4 clicked", Toast.LENGTH_SHORT).show()
        }

        // Optional: set a listener to handle the opening and closing of the menu
        fabMenu.setOnMenuToggleListener { opened ->
            if (opened) {
                Toast.makeText(this, "Menu is opened", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Menu is closed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
