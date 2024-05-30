/*
package com.example.shimmerrecyclerview

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView

class OnlinePDFViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_online_pdfview)

        val sentences = arrayOf(
            "KSFO FLW KSAN 7000ft",
            "KSFO KSAN 32.3N/99W N35388 1:45p",
            "KSFO FLW/320/15 KSAN 7000",
            "KUZA ELW/321R/30 NELLO KEDC +45",
            "KSAN MZB MZB293/SLI148 SLI",
            "D KSGR",
            "Disney World",
            "525 W 20th Ave, Oshkosh",
            "Zürich"
        )

        val listView: ListView = findViewById(R.id.listView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, sentences)
        listView.adapter = adapter

        showFullScreenDialog()
    }

    private fun showFullScreenDialog() {
        val dialog = Dialog(this, R.style.FullScreenDialogStyle)
        dialog.setContentView(R.layout.dialog_fullscreen)

        val closeButton: ImageButton = dialog.findViewById(R.id.close_button)
        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }
}*/
