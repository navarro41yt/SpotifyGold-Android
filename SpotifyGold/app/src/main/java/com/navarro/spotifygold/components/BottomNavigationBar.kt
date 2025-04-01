package com.navarro.spotifygold.components

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.navarro.spotifygold.models.ItemsBottomNav
import com.navarro.spotifygold.ui.theme.Black0
import com.navarro.spotifygold.ui.theme.*
import com.navarro.spotifygold.ui.theme.Black70
import com.navarro.spotifygold.utils.currentRoute

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    context: Context = LocalContext.current
) {
    val icons = listOf(
        ItemsBottomNav.Home(context),
        ItemsBottomNav.Search(context),
        ItemsBottomNav.Library(context)
    )
    BottomAppBar(
        containerColor = Color.Transparent,
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier
            .padding(0.dp)
            .height(80.dp)
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            contentColor = Color.White,
            modifier = Modifier.padding(0.dp)
        ) {
            Row( // Another row to change the background color
                modifier = Modifier.background(
                    brush = BlackGradient
                )
            ) {
                icons.forEach { item ->
                    val selected = currentRoute(navController = navController) == item.route
                    Log.d("BottomNavigationBar", "Selected: $selected")
                    NavigationBarItem(
                        selected = false,
                        onClick = {
                            navController.navigate(item.route)
                        },
                        icon = {
                            Icon(
                                modifier = Modifier
                                    .padding(0.dp)
                                    .fillMaxSize(0.4f),
                                imageVector = (if (selected) item.filledIcon
                                else item.outlinedIcon),
                                contentDescription = item.title,
                                tint = (if (selected) Black0
                                else Black70)
                            )
                        }, label = {
                            Text(
                                text = item.title,
                                color = (if (selected) Black0
                                else Black70)
                            )
                        }, interactionSource = MutableInteractionSource()
                    )
                }
            }
        }
    }
}