package com.navarro.spotifygold.views

import android.util.Log
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.navarro.spotifygold.R
import com.navarro.spotifygold.components.global.InteractableIconButton
import com.navarro.spotifygold.components.home.HomeItem
import com.navarro.spotifygold.entities.AudioDRO
import com.navarro.spotifygold.services.MediaPlayerSingleton
import com.navarro.spotifygold.services.preferences.PreferencesKeys
import com.navarro.spotifygold.services.preferences.PreferencesService
import com.navarro.spotifygold.services.readMusic
import com.navarro.spotifygold.services.room.DatabaseProvider
import com.navarro.spotifygold.ui.theme.Black0
import com.navarro.spotifygold.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@Composable
fun HomeScreen(
    queue: MutableList<AudioDRO>,
    current: MutableState<AudioDRO>
) {
    val context = LocalContext.current

    val db = DatabaseProvider.getDatabase(context)

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp, 0.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = Constants.defaultImage,
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.size(20.dp))
            Text(
                text = stringResource(id = R.string.navigation_home),
                color = Black0,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            InteractableIconButton(icon = Icons.AutoMirrored.Filled.Logout) {
                Log.d("HomeScreen", "Logging out.")
                val pair = PreferencesKeys.TOKEN

                MediaPlayerSingleton.pause()
                PreferencesService.setProperty(
                    context,
                    pair.key,
                    pair.defaultValue
                )
                PreferencesService.isLogged.value = false
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.home_most_played),
                color = Black0,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            val mostPlayedSongs = runBlocking {
                withContext(Dispatchers.IO) {
                    db.metadataRepo().getMostPlayed()
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                mostPlayedSongs.forEach { song ->
                    HomeItem(
                        song = song,
                        type = stringResource(id = R.string.global_song),
                        currentSong = current
                    ) {
                        val audioDRO = readMusic(song)

                        queue.clear()
                        queue.add(audioDRO)
                        current.value = audioDRO
                        MediaPlayerSingleton.play(
                            context,
                            audioDRO
                        )
                    }
                }
            }
        }

        Column {
            Text(
                text = stringResource(id = R.string.home_jump_back_in),
                color = Black0,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            val mostPlayedSongs = runBlocking {
                withContext(Dispatchers.IO) {
                    db.metadataRepo().getLeastPlayed()
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                mostPlayedSongs.forEach { song ->
                    HomeItem(
                        song = song,
                        type = stringResource(id = R.string.global_song),
                        currentSong = current
                    ) {
                        val audioDRO = readMusic(song)

                        queue.clear()
                        queue.add(audioDRO)
                        current.value = audioDRO
                        MediaPlayerSingleton.play(
                            context,
                            audioDRO
                        )
                    }
                }
            }
        }

        Column {
            Text(
                text = stringResource(id = R.string.home_podcasts),
                color = Black0,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            val mostPlayedSongs = runBlocking {
                withContext(Dispatchers.IO) {
                    db.metadataRepo().getPodcasts()
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                mostPlayedSongs.forEach { song ->
                    HomeItem(
                        song = song,
                        type = stringResource(id = R.string.global_podcast),
                        currentSong = current
                    ) {
                        val audioDRO = readMusic(song)

                        queue.clear()
                        queue.add(audioDRO)
                        current.value = audioDRO
                        MediaPlayerSingleton.play(
                            context,
                            audioDRO
                        )
                    }
                }
            }
        }

        Column {
            Text(
                text = stringResource(id = R.string.home_made_for_you),
                color = Black0,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            val mostPlayedSongs = runBlocking {
                withContext(Dispatchers.IO) {
                    db.metadataRepo().getRandom(8)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                mostPlayedSongs.forEach { song ->
                    HomeItem(
                        song = song,
                        type = stringResource(id = R.string.global_song),
                        currentSong = current
                    ) {
                        val audioDRO = readMusic(song)

                        queue.clear()
                        queue.add(audioDRO)
                        current.value = audioDRO
                        MediaPlayerSingleton.play(
                            context,
                            audioDRO
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.size(200.dp))
    }
}
