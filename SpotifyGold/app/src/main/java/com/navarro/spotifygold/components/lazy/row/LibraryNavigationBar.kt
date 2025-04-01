package com.navarro.spotifygold.components.lazy.row

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.navarro.spotifygold.models.ItemsBottomNav
import com.navarro.spotifygold.models.ItemsLibraryNav
import com.navarro.spotifygold.models.LibraryModes
import com.navarro.spotifygold.ui.theme.Black0
import com.navarro.spotifygold.ui.theme.*
import com.navarro.spotifygold.ui.theme.Black70
import com.navarro.spotifygold.utils.currentRoute

@Composable
fun LibraryNavigationBar(
    viewMode: MutableState<LibraryModes>
) {
    val context = LocalContext.current

    val icons = listOf(
        ItemsLibraryNav.Songs(context),
        ItemsLibraryNav.Artists(context),
        ItemsLibraryNav.Playlists(context)
    )
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .fillMaxWidth().padding(0.dp, 0.dp, 0.dp, 10.dp),
        content = {
            items(icons.size) { index ->
                val item = icons[index]
                val isSelected = viewMode.value == item.mode

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .border(
                            width = 1.dp,
                            color = if (isSelected) Gold50 else Black70,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(10.dp, 5.dp)
                        .clickable {
                            viewMode.value = item.mode
                        }
                ) {
                    Icon(
                        imageVector = if (isSelected) item.filledIcon else item.outlinedIcon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = if (isSelected) Black0 else Black70
                    )
                    Text(
                        text = item.title,
                        color = if (isSelected) Black0 else Black70,
                        fontSize = 15.sp
                    )
                }
            }
        }
    )
}