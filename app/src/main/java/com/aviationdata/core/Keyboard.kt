package com.aviationdata.core

import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

fun dismissKeyboard(activity: AppCompatActivity) {
    val inputManager = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

    if (inputManager.isAcceptingText) {
        inputManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
    }
}