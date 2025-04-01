package com.navarro.spotifygold.services.preferences

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.navarro.spotifygold.services.StaticToast
import seeIfHasPermission
import java.io.File
import java.util.Properties

object PreferencesService {

    val hasAcceptedPermissions = mutableStateOf(seeIfHasPermission())
    val isLogged = mutableStateOf(false)

    private const val FILE_NAME = "spotifygold.properties"

    fun init(
        context: Context
    ) {
        val file = File(context.filesDir, FILE_NAME)
        if (!file.exists()) {
            file.createNewFile()
        } else {
            Log.d("PreferencesService", "Preferences file already exists.")
        }
    }

    fun getProperties(
        context: Context
    ): Properties {
        val file = File(context.filesDir, FILE_NAME)
        val properties = Properties()
        try {
            properties.load(file.inputStream())
        } catch (e: Exception) {
            StaticToast.showToast("Error loading properties file.")
            Log.e("PreferencesService", "Error loading properties file.")
        }
        return properties
    }

    fun setProperties(
        context: Context,
        properties: Properties
    ) {
        val file = File(context.filesDir, FILE_NAME)
        properties.store(file.outputStream(), null)
    }

    fun getProperty(
        context: Context,
        key: String
    ): String? {
        val properties = getProperties(context)
        return try {
            properties.getProperty(key)
        } catch (e: NullPointerException) {
            StaticToast.showToast("Error getting property.")
            Log.e("PreferencesService", "Error getting property.")
            null
        }
    }

    fun setProperty(
        context: Context,
        key: String,
        value: String
    ) {
        val properties = getProperties(context)
        properties.setProperty(key, value)
        setProperties(context, properties)
    }
}
