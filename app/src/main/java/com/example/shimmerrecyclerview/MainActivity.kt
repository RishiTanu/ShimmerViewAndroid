package com.example.shimmerrecyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout

class MainActivity : AppCompatActivity() {

    private lateinit var shimmerLayout: ShimmerFrameLayout
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private var dataList = listOf<DummyItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        shimmerLayout = findViewById(R.id.shimmer_layout)
        val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recycler_view)

        // Setup initial shimmer adapter
        recyclerViewAdapter = RecyclerViewAdapter(dataList, showShimmer = true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = recyclerViewAdapter

        // Simulate loading data with delay
        Handler(Looper.getMainLooper()).postDelayed({
            loadData()
        }, 3000)
    }

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