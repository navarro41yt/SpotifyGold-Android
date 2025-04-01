package com.navarro.spotifygold.components.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.unit.dp

@Composable
fun LabelTextField(
    label: String,
    value: MutableState<String>,
    isPassword: Boolean = false
) {
    Column (
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
        )
        SpotifyGoldTextField(
            value = value,
            placeholder = label,
            isPassword = isPassword
        )
    }
}