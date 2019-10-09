package com.aviationdata.core.utility

import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun dismissKeyboard(fragment: Fragment) {
    val activity = fragment.requireActivity()
    val inputManager = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

    if (inputManager.isAcceptingText) {
        inputManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
    }
}