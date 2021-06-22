package com.jns.foodie.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat

fun noInternetDialogBox(context: Context): androidx.appcompat.app.AlertDialog.Builder {
    val alterDialog = androidx.appcompat.app.AlertDialog.Builder(context)
    alterDialog.setTitle("No Internet")
    alterDialog.setMessage("Check Internet Connection!")
    alterDialog.setPositiveButton("Open Settings") { _, _ ->
        val settingsIntent = Intent(Settings.ACTION_SETTINGS)
        context.startActivity(settingsIntent)
    }
    alterDialog.setNegativeButton("Exit") { _, _ ->
        ActivityCompat.finishAffinity(context as Activity)
    }
    alterDialog.setCancelable(false)
    alterDialog.create()
    return alterDialog
}

fun responseErrorToast(context: Context, it: String) {
    if (it == "com.android.volley.TimeoutError") {
        Toast.makeText(
            context,
            "Connection Timeout !!",
            Toast.LENGTH_SHORT
        ).show()
    } else
        Toast.makeText(
            context,
            "Some Error occurred!!!",
            Toast.LENGTH_SHORT
        ).show()
}