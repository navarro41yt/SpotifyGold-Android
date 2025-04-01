package com.navarro.spotifygold.components.lazy.column

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.navarro.spotifygold.R
import com.navarro.spotifygold.components.global.IconInfo
import com.navarro.spotifygold.components.global.InteractableIconButton
import com.navarro.spotifygold.entities.AudioDRO
import com.navarro.spotifygold.services.MediaPlayerSingleton.play
import com.navarro.spotifygold.services.StaticToast
import com.navarro.spotifygold.services.getInfo
import com.navarro.spotifygold.services.room.DatabaseProvider
import com.navarro.spotifygold.ui.theme.Black0
import com.navarro.spotifygold.ui.theme.Black20
import com.navarro.spotifygold.ui.theme.Black80
import com.navarro.spotifygold.ui.theme.Gold50
import com.navarro.spotifygold.utils.addDotsToNumber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException

@Composable
fun SongsLazyColumn(
    files: MutableList<AudioDRO>,
    queue: MutableList<AudioDRO>,
    current: MutableState<AudioDRO>
) {
    val context = LocalContext.current

    LazyColumn(content = {
        items(files.size) { index ->

            val audioDRO = files[index]

            val isPlaying = current.value.route == audioDRO.route

            val dropdownExpanded = remember { mutableStateOf(false) }
            val showDialog = remember { mutableStateOf(false) }
            val showDetailsDialog = remember { mutableStateOf(false) }
            val selectedAudioDRO = remember { mutableStateOf<AudioDRO?>(null) }

            if (showDialog.value) {
                AlertDialog(
                    containerColor = Black80,
                    onDismissRequest = { showDialog.value = false },
                    title = { Text(stringResource(id = R.string.global_delete_song)) },
                    text = {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Text(
                                stringResource(id = R.string.global_delete_song_subtitle),
                                color = Black0,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                selectedAudioDRO.value!!.getSafeTitle(),
                            )
                            Text(
                                selectedAudioDRO.value!!.getSafeArtist(),
                                color = Gold50
                            )
                            Text(
                                selectedAudioDRO.value!!.route,
                                color = Black0
                            )
                        }
                    },
                    confirmButton = {
                        Button(onClick = {
                            selectedAudioDRO.value?.let {
                                File(it.route).delete()
                                files.remove(it)
                                queue.remove(it)
                                if (audioDRO.metadata != null) {
                                    runBlocking {
                                        withContext(Dispatchers.IO) {
                                            DatabaseProvider.getDatabase(context).metadataRepo()
                                                .deleteMetadata(it.metadata!!.id)
                                        }
                                    }
                                }
                            }
                            showDialog.value = false
                        }) {
                            Text(stringResource(id = R.string.global_delete))
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showDialog.value = false }) {
                            Text(stringResource(id = R.string.global_cancel))
                        }
                    }
                )
            }

            if (showDetailsDialog.value) {
                AlertDialog(
                    containerColor = Black80,
                    onDismissRequest = { showDetailsDialog.value = false },
                    title = { Text(stringResource(id = R.string.global_details)) },
                    text = {
                        selectedAudioDRO.value?.let { audio ->
                            Column {
                                Text(
                                    audio.getSafeTitle(),
                                    color = Black0
                                )
                                Text(
                                    audio.getSafeArtist(),
                                    color = Gold50
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    audio.getSafeUploadAt()
                                )
                                IconInfo(
                                    text = addDotsToNumber(audio.getSafeViews()),
                                    color = Black20,
                                    icon = Icons.Filled.Visibility
                                )
                                IconInfo(
                                    text = addDotsToNumber(audio.getSafeLikes()),
                                    color = Black20,
                                    icon = Icons.Filled.ThumbUp
                                )
                                IconInfo(
                                    text = audio.getSafeLink(),
                                    icon = Icons.Filled.Language,
                                    color = Gold50,
                                    modifier = Modifier.clickable {
                                        val intent = Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse(audio.getSafeLink())
                                        )
                                        context.startActivity(intent)
                                    }
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                AsyncImage(
                                    model = audio.getSafeThumbnail(),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .aspectRatio(1f)
                                )
                            }
                        }
                    },
                    confirmButton = {
                        Button(onClick = { showDetailsDialog.value = false }) {
                            Text(stringResource(id = R.string.global_ok))
                        }
                    }
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 10.dp)
                    .height(50.dp)
                    .clickable {
                        try {
                            play(
                                context,
                                audioDRO
                            )
                        } catch (e: FileNotFoundException) {
                            StaticToast.showToast(
                                context.getString(R.string.error_file_not_found)
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                            StaticToast.showToast(
                                context.getString(R.string.error_unknown)
                            )
                        }

                        queue.clear()
                        queue.addAll(files)
                        current.value = audioDRO

                        try {
                            getInfo(context, audioDRO.metadata!!.id)
                        } catch (e: NullPointerException) {
                            getInfo(
                                context,
                                audioDRO.route
                                    .split("/")
                                    .last()
                                    .split(".")[1]
                            )
                        } catch (e: Exception) {
                            StaticToast.showToast(
                                context.getString(R.string.error_file_not_downloaded)
                            )
                        }
                    }) {
                Image(
                    painter = rememberAsyncImagePainter(
                        audioDRO.getSafeThumbnail()
                    ),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
                Column(
                    verticalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    Text(
                        text = audioDRO.metadata?.title ?: audioDRO.route.split("/").last(),
                        color = if (isPlaying) Gold50 else Black0,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(20.dp, 0.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = audioDRO.metadata?.author?.name ?: audioDRO.route,
                        color = Black20,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(20.dp, 0.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Column {
                    InteractableIconButton(
                        icon = Icons.Filled.MoreVert,
                        onClick = {
                            dropdownExpanded.value = true
                        }
                    )
                    DropdownMenu(
                        expanded = dropdownExpanded.value,
                        onDismissRequest = { dropdownExpanded.value = false },
                        modifier = Modifier.background(Black80),
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(id = R.string.global_add_to_playlist)) },
                            onClick = {
                                StaticToast.showToast(
                                    context.getString(R.string.error_not_implemented_yet)
                                )
                                dropdownExpanded.value = false
                            })
                        DropdownMenuItem(
                            text = { Text(stringResource(id = R.string.global_details)) },
                            onClick = {
                                selectedAudioDRO.value = audioDRO
                                showDetailsDialog.value = true
                                dropdownExpanded.value = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(id = R.string.global_delete)) },
                            onClick = {
                                selectedAudioDRO.value = audioDRO
                                showDialog.value = true
                                dropdownExpanded.value = false
                            }
                        )
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(200.dp))
        }
    })
}