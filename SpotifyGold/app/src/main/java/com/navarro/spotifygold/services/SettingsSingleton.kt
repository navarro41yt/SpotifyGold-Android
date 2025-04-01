package com.navarro.spotifygold.services

import androidx.compose.runtime.mutableStateOf

object SettingsSingleton {
    val loop = mutableStateOf(false)
    val shuffle = mutableStateOf(false)
}