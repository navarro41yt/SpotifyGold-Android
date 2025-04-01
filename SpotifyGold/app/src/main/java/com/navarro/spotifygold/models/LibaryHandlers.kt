package com.navarro.spotifygold.models

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.LocalContext
import com.navarro.spotifygold.R
import com.navarro.spotifygold.services.StaticToast
import com.navarro.spotifygold.entities.ArtistDRO
import com.navarro.spotifygold.entities.AudioDRO
import com.navarro.spotifygold.entities.metadata.AuthorEntity
import com.navarro.spotifygold.services.readArtists
import com.navarro.spotifygold.services.readMusicFolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun LibraryHandlers(
    selectedArtist: MutableState<AuthorEntity?>,
    mode: MutableState<LibraryModes>,
    filter: MutableState<String>,
    searchMode: MutableState<Boolean>,
    filesFiltered: SnapshotStateList<AudioDRO>,
    artistsFiltered: SnapshotStateList<ArtistDRO>
) {
    val context = LocalContext.current

    val files = remember { mutableListOf<AudioDRO>() }
    val artists = remember { mutableListOf<ArtistDRO>() }

    BackHandler(
        enabled = selectedArtist.value != null
    ) {
        selectedArtist.value = null
        mode.value = LibraryModes.ARTISTS
    }

    BackHandler(
        enabled = searchMode.value
    ) {
        filter.value = ""
        searchMode.value = false
    }

    LaunchedEffect(mode.value) {
        when (mode.value) {
            LibraryModes.SONGS -> {
                val musicFiles = withContext(Dispatchers.IO) {
                    readMusicFolder(context, selectedArtist.value)
                }
                files.clear()
                files.addAll(musicFiles)
                filesFiltered.clear()
                filesFiltered.addAll(files)
            }

            LibraryModes.ARTISTS -> {
                selectedArtist.value = null

                val artistsReturned = withContext(Dispatchers.IO) {
                    readArtists(context)
                }
                artists.clear()
                artists.addAll(artistsReturned)
                artistsFiltered.clear()
                artistsFiltered.addAll(artists)
            }

            else -> {
                StaticToast.showToast(context.getString(R.string.error_not_implemented_yet))
            }
        }
    }

    LaunchedEffect(filter.value) {
        filesFiltered.clear()
        artistsFiltered.clear()

        if (filter.value.isEmpty()) {
            filesFiltered.addAll(files)
            artistsFiltered.addAll(artists)
        } else {
            filesFiltered.addAll(
                files.filter {
                    it.metadata?.title?.contains(filter.value, ignoreCase = true) ?: false
                }
            )

            artistsFiltered.addAll(
                artists.filter {
                    it.author.name.contains(filter.value, ignoreCase = true)
                }
            )
        }
    }
}
