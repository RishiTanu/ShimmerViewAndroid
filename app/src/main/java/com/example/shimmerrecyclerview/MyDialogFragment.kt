package com.example.shimmerrecyclerview


import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.app.AlertDialog
import android.content.DialogInterface

class MyDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("This is a Dialog")
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                    // Do something on OK button press
                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                    // Do something on Cancel button press
                })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
