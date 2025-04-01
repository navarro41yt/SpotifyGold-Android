package com.navarro.spotifygold.utils

import android.content.Context
import android.os.Build
import android.os.Environment
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker

fun hasManageExternalStoragePermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        Environment.isExternalStorageManager()
    } else {
        ContextCompat.checkSelfPermission(
            context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PermissionChecker.PERMISSION_GRANTED
    }
}

fun formatTimeMillis(milliseconds: Long): String {
    val hours = (milliseconds / (1000 * 60 * 60)).toInt()
    val minutes = ((milliseconds % (1000 * 60 * 60)) / (1000 * 60)).toInt()
    val seconds = ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000).toInt()

    return if (hours > 0) {
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%02d:%02d", minutes, seconds)
    }
}

fun formatTimeSeconds(seconds: Long): String {
    val hours = (seconds / 3600).toInt()
    val minutes = ((seconds % 3600) / 60).toInt()
    val secs = (seconds % 60).toInt()

    return if (hours > 0) {
        String.format("%02d:%02d:%02d", hours, minutes, secs)
    } else {
        String.format("%02d:%02d", minutes, secs)
    }
}

fun formatViews(views: Long): String {
    return formatNumber(views)
}

fun formatLikes(likes: Long): String {
    val ftt = formatNumber(likes)

    // If equals 0 return "?"
    return if (ftt == "0") "?" else ftt
}

fun formatNumber(number: Long): String {
    return when {
        number < 1000 -> number.toString()
        number < 1000000 -> "${number / 1000}k"
        number < 1000000000 -> "${number / 1000000}m"
        else -> "${number / 1000000000}B"
    }
}

/**
 * Format time in milliseconds to a readable format
 * @param time Time in ms
 * @return Formatted time
 */
fun formatTime(time: Int): String {
    val hours = time / 3600000
    val minutes = (time % 3600000) / 60000
    val seconds = (time % 60000) / 1000

    return if (hours > 0) {
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%02d:%02d", minutes, seconds)
    }
}

fun randomString(length: Int): String {
    val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return List(length) { charset.random() }.joinToString("")
}

fun addDotsToNumber(number: Long): String {
    return number.toString().replace(Regex("(\\d)(?=(\\d{3})+\$)"), "\$1.")
}
