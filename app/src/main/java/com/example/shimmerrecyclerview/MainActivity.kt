package com.example.shimmerrecyclerview
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.lifecycle.Observer
import com.arkapp.iosdatettimepicker.ui.DialogDateTimePicker
import com.arkapp.iosdatettimepicker.utils.OnDateTimeSelectedListener
import com.facebook.shimmer.ShimmerFrameLayout
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinner: Spinner = findViewById(R.id.spinner)
        val items = arrayOf(
            "Select category", "Item 1", "Item 2", "Item 3", "Item 4",
            "Item 5", "Item 6", "Item 7", "Item 8", "Item 9", "Item 10"
        )
        val adapter = CustomSpinnerAdapter(this, items) { selectedItems ->
            // Handle the selected items callback here
            onItemSelected(selectedItems)
        }
        spinner.adapter = adapter

        //for single working
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        val spinner1: AppCompatSpinner = findViewById(R.id.spinnerNew)

        val items11 = arrayOf(
            "Select category", "Item 1", "Item 2", "Item 3", "Item 4",
            "Item 5", "Item 6", "Item 7", "Item 8", "Item 9", "Item 10"
        )

        val adapter11 = CustomSpinnerMultipleAdapter(this, items11) { selectedItems ->
            // Handle the selected items callback here
            onItemsSelectedMultiple(selectedItems)
        }
        spinner1.adapter = adapter11

        // Set initial selection to first item
        spinner.setSelection(0)

        spinnerEdt()
    }

    private fun onItemsSelectedMultiple(selectedItems: List<String>) {
        // Callback logic here
        println("Selected items: $selectedItems")
    }

    private fun onItemSelected(position: Int) {
        // Callback logic here
        if (position > 0) {
            println("Selected item: ${position} [position]}")
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////


    private fun spinnerEdt(){
        val spinner: Spinner = findViewById(R.id.spinnerEdit)
        val items = arrayOf(
            "Select Other", "Item 1", "Item 2", "Item 3", "Item 4",
            "Item 5", "Item 6", "Item 7", "Item 8", "Item 9", "Item 10"
        )

        val adapter = CustomSpinnerWithEditTextAdapter(this, items)
        spinner.adapter = adapter

        // Set initial selection to first item
        spinner.setSelection(0)
    }
}

// Assuming Legs is a data class containing lists of LatLng points and collision points
data class Leg(val geoPoints: List<LatLng>, val collisionPoints: List<LatLng>)

// GoogleMap object should be globally initialized
lateinit var map: GoogleMap

// Function to draw polylines based on comparison for all legs
data class Leg(val geoPoints: List<LatLng>, val collisionPoints: List<LatLng>)

// Global GoogleMap object should be initialized
lateinit var map: GoogleMap

// Coroutine scope for launching background tasks
val scope = CoroutineScope(Dispatchers.Main)

fun drawPolylines(legs: List<Leg>) {
    scope.launch {
        // Process each leg on a background thread
        withContext(Dispatchers.Default) {
            for (leg in legs) {
                val geoPoints = leg.geoPoints
                val collisionPoints = leg.collisionPoints

                for (i in 0 until geoPoints.size - 1) {
                    val startPoint = geoPoints[i]
                    val endPoint = geoPoints[i + 1]

                    val color = if (collisionPoints.contains(startPoint) && collisionPoints.contains(endPoint)) {
                        Color.RED
                    } else {
                        Color.GREEN
                    }

                    // Drawing on the main thread
                    withContext(Dispatchers.Main) {
                        map.addPolyline(
                            PolylineOptions()
                                .add(startPoint, endPoint)
                                .width(5f)
                                .color(color)
                        )
                    }
                }
            }
        }
    }
}

// Example usage
val legs: List<Leg> = // Initialize with your legs containing geoPoints and collisionPoints
    drawPolylines(legs)