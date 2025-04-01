package com.navarro.spotifygold.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.navarro.spotifygold.R
import com.navarro.spotifygold.components.global.InteractableIconButton
import com.navarro.spotifygold.components.lazy.column.ArtistsLazyColumn
import com.navarro.spotifygold.components.lazy.column.SongsLazyColumn
import com.navarro.spotifygold.components.lazy.row.LibraryNavigationBar
import com.navarro.spotifygold.components.library.FilterBar
import com.navarro.spotifygold.entities.ArtistDRO
import com.navarro.spotifygold.entities.AudioDRO
import com.navarro.spotifygold.entities.metadata.AuthorEntity
import com.navarro.spotifygold.models.LibraryHandlers
import com.navarro.spotifygold.models.LibraryModes
import com.navarro.spotifygold.services.StaticToast
import com.navarro.spotifygold.services.getAllInfo
import com.navarro.spotifygold.ui.theme.Black0
import com.navarro.spotifygold.utils.Constants

@Composable
fun LibraryScreen(
    queue: MutableList<AudioDRO>,
    current: MutableState<AudioDRO>
) {
    val context = LocalContext.current

    val mode = remember { mutableStateOf(LibraryModes.SONGS) }

    val searchMode = remember { mutableStateOf(false) }
    val filter = remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    val filesFiltered = remember { mutableStateListOf<AudioDRO>() }
    val artistsFiltered = remember { mutableStateListOf<ArtistDRO>() }
    val selectedArtist = remember { mutableStateOf<AuthorEntity?>(null) }

    LibraryHandlers(
        selectedArtist = selectedArtist,
        mode = mode,
        filter = filter,
        searchMode = searchMode,
        filesFiltered = filesFiltered,
        artistsFiltered = artistsFiltered
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp, 0.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
        ) {
            if (searchMode.value) // Search
                FilterBar(filter = filter, searchMode = searchMode, focusRequester = focusRequester)
            else { // Header
                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    AsyncImage(
                        model = Constants.defaultImage,
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                    )
                    Text(
                        text = if (selectedArtist.value == null) stringResource(id = R.string.navigation_library)
                        else "${stringResource(id = R.string.global_artist)}: " +
                                selectedArtist.value!!.name,
                        color = Black0,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    InteractableIconButton(icon = Icons.Filled.Download, onClick = {
                        StaticToast.showToast(context.getString(R.string.library_download_all_metadata))
                        val ids = filesFiltered.map {
                            it.route.split("/").last().split(".")[1]
                        }
                        getAllInfo(context, ids)

                        mode.value = LibraryModes.ARTISTS
                        mode.value = LibraryModes.SONGS
                    })
                    InteractableIconButton(icon = Icons.Filled.Search, onClick = {
                        searchMode.value = true
                    })
                    InteractableIconButton(icon = Icons.Filled.Add, onClick = {
                        StaticToast.showToast(context.getString(R.string.error_not_implemented_yet))
                    })
                }
            }
        }
        LibraryNavigationBar(
            viewMode = mode
        )
        when (mode.value) {
            LibraryModes.SONGS -> {
                SongsLazyColumn(
                    files = filesFiltered,
                    queue = queue,
                    current = current
                )
            }

            LibraryModes.ARTISTS -> {
                ArtistsLazyColumn(
                    artists = artistsFiltered,
                    selectedArtist = selectedArtist,
                    viewMode = mode
                )
            }

            else -> {
                StaticToast.showToast(context.getString(R.string.error_not_implemented_yet))
            }
        }
    }
}
