package com.navarro.spotifygold.components.global

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.navarro.spotifygold.ui.theme.Black0

@Composable
fun InteractableIconButton(
    icon: ImageVector,
    tint: Color = Black0,
    contentDescription: String = "Icon-$icon",
    size: Dp = 28.dp,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .padding(0.dp)
    ) {
        Icon(
            modifier = Modifier
                .padding(0.dp)
                .size(size),
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint
        )
    }
}