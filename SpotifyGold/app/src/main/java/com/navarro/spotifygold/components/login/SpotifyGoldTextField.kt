package com.navarro.spotifygold.components.login

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import com.navarro.spotifygold.components.global.InteractableIconButton
import com.navarro.spotifygold.ui.theme.Black0
import com.navarro.spotifygold.ui.theme.Black60
import com.navarro.spotifygold.ui.theme.Black90
import com.navarro.spotifygold.ui.theme.Transparent

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SpotifyGoldTextField(
    value: MutableState<String>, placeholder: String, isPassword: Boolean = false
) {
    var passwordVisible by remember { mutableStateOf(!isPassword) }

    TextField(
        value = value.value,
        onValueChange = { value.value = it },
        placeholder = {
            Text(
                text = placeholder,
                color = Black60,
            )
        },
        textStyle = TextStyle(color = Black0, fontSize = 18.sp),
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            textColor = Black0,
            containerColor = Black90,
            focusedIndicatorColor = Transparent,
            unfocusedIndicatorColor = Transparent,
            disabledIndicatorColor = Transparent
        ),
        trailingIcon = {
            if (isPassword) {
                InteractableIconButton(
                    icon = if (passwordVisible) Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff
                ) {
                    passwordVisible = !passwordVisible
                }
            } else {
                if (value.value.isNotEmpty()) {
                    InteractableIconButton(icon = Icons.Filled.Close, onClick = {
                        value.value = ""
                    })
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text
        ),
        visualTransformation = if (!passwordVisible) PasswordVisualTransformation()
        else VisualTransformation.None,
        modifier = Modifier.fillMaxWidth()
    )
}