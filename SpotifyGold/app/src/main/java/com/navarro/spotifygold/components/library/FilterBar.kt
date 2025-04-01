package com.navarro.spotifygold.components.library

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.navarro.spotifygold.R
import com.navarro.spotifygold.components.global.InteractableIconButton
import com.navarro.spotifygold.ui.theme.Black0
import com.navarro.spotifygold.ui.theme.Black60
import com.navarro.spotifygold.ui.theme.Transparent
import kotlinx.coroutines.delay

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun FilterBar(
    filter: MutableState<String>,
    searchMode: MutableState<Boolean>,
    focusRequester: FocusRequester
) {
    TextField(
        value = filter.value,
        onValueChange = { filter.value = it },
        placeholder = {
            Text(
                text = stringResource(id = R.string.search_searchbox_placeholder),
                color = Black60,
            )
        },
        textStyle = TextStyle(color = Black0, fontSize = 18.sp),
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            textColor = Black0,
            containerColor = Transparent,
            focusedIndicatorColor = Transparent,
            unfocusedIndicatorColor = Transparent,
            disabledIndicatorColor = Transparent
        ),
        leadingIcon = {
            InteractableIconButton(
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                onClick = {
                    filter.value = ""
                    searchMode.value = false
                }
            )
        },
        trailingIcon = {
            if (filter.value.isNotEmpty()) {
                InteractableIconButton(
                    icon = Icons.Filled.Close,
                    onClick = {
                        filter.value = ""
                    }
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
    )
    LaunchedEffect(Unit) {
        delay(100)
        focusRequester.requestFocus()
    }
}