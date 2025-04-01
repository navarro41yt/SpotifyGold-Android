package com.navarro.spotifygold.components.global

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.navarro.spotifygold.ui.theme.Black0
import com.navarro.spotifygold.ui.theme.Gold50

@Composable
fun ToggeableIconButton(
    icon: ImageVector,
    normalTint: Color = Black0,
    activeTint: Color = Gold50,
    condition: Boolean = false,
    contentDescription: String = "Icon-$icon",
    size: Dp = 28.dp,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
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
                tint = if (condition) activeTint else normalTint
            )
        }
        Spacer(modifier = Modifier.size(4.dp))
        if (condition) {
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .background(
                        color = activeTint,
                        shape = CircleShape
                    )
            )
        }
    }
}