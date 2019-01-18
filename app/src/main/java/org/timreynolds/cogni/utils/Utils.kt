@file:JvmName("Utils")

package org.timreynolds.cogni.utils

import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Utils
 */


val Context.connectivityManager: ConnectivityManager?
    get() = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun isEmailValid(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}