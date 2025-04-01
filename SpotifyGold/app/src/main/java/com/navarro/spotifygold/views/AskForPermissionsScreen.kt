package com.navarro.spotifygold.views

import RequestStoragePermission
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.navarro.spotifygold.R
import com.navarro.spotifygold.ui.theme.Black0
import com.navarro.spotifygold.ui.theme.Black100
import com.navarro.spotifygold.ui.theme.Black20
import com.navarro.spotifygold.utils.Constants

@Composable
fun AskForPermissionsScreen(
    onPermissionResult: (Boolean) -> Unit
) {
    val showPermissionRequest = remember { mutableStateOf(false) }

    if (showPermissionRequest.value) {
        RequestStoragePermission { granted ->
            onPermissionResult(granted)
            showPermissionRequest.value = false
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Black100)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(90.dp, 0.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = Constants.defaultImage),
                contentDescription = "App Icon \"Spotify Gold\""
            )
            Text(
                text = stringResource(id = R.string.app_permissions_title),
                fontSize = 20.sp,
                color = Black0
            )
            Text(
                text = stringResource(id = R.string.app_permissions_message),
                fontSize = 14.sp,
                color = Black20
            )
            Button(onClick = {
                showPermissionRequest.value = true
            }) {
                Text(
                    text = stringResource(id = R.string.app_permissions_button),
                    fontSize = 14.sp,
                    color = Black0
                )
            }
        }
    }
}
