package com.navarro.spotifygold.services

import android.widget.Toast

class StaticToast {
    companion object {
        lateinit var toast: Toast

        fun showToast(message: String, long: Boolean = false) {
            toast.setText(message)
            toast.duration = if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
            toast.show()
        }
    }
}