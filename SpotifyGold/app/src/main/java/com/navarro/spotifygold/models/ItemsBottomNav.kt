package com.navarro.spotifygold.models

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.navarro.spotifygold.R
import com.navarro.spotifygold.navigation.Navigation

sealed class ItemsBottomNav (
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector,
    val title: String,
    val route: String
) {
    class Home(context: Context) : ItemsBottomNav(
        Icons.Filled.Home,
        Icons.Outlined.Home,
        context.getString(R.string.navigation_home),
        Navigation.HOME.name
    )

    class Search(context: Context) : ItemsBottomNav(
        Icons.Filled.Search,
        Icons.Outlined.Search,
        context.getString(R.string.navigation_search),
        Navigation.SEARCH.name
    )

    class Library(context: Context) : ItemsBottomNav(
        Icons.Filled.LibraryMusic,
        Icons.Outlined.LibraryMusic,
        context.getString(R.string.navigation_library),
        Navigation.LIBRARY.name
    )
}
