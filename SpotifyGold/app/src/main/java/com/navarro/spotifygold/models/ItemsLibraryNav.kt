package com.navarro.spotifygold.models

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.SavedSearch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.navarro.spotifygold.R
import com.navarro.spotifygold.navigation.Navigation

sealed class ItemsLibraryNav (
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector,
    val title: String,
    val mode: LibraryModes
) {
    class Songs(context: Context) : ItemsLibraryNav(
        Icons.Filled.MusicNote,
        Icons.Outlined.MusicNote,
        context.getString(R.string.global_songs),
        LibraryModes.SONGS
    )

    class Artists(context: Context) : ItemsLibraryNav(
        Icons.Filled.People,
        Icons.Outlined.People,
        context.getString(R.string.global_artists),
        LibraryModes.ARTISTS
    )

    class Playlists(context: Context) : ItemsLibraryNav(
        Icons.Filled.LibraryMusic,
        Icons.Outlined.LibraryMusic,
        context.getString(R.string.global_playlists),
        LibraryModes.PLAYLISTS
    )
}
