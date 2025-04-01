package com.navarro.spotifygold.components.lazy.column

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.navarro.spotifygold.R
import com.navarro.spotifygold.entities.ArtistDRO
import com.navarro.spotifygold.entities.metadata.AuthorEntity
import com.navarro.spotifygold.models.LibraryModes
import com.navarro.spotifygold.ui.theme.Black0
import com.navarro.spotifygold.ui.theme.Gold50

@Composable
fun ArtistsLazyColumn(
    artists: List<ArtistDRO>,
    selectedArtist: MutableState<AuthorEntity?>,
    viewMode: MutableState<LibraryModes>
) {
    val context = LocalContext.current

    LazyColumn(content = {
        items(artists.size) { index ->

            val artistDRO = artists[index]

            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 10.dp)
                    .height(50.dp)
                    .clickable {
                        selectedArtist.value = artistDRO.author
                        viewMode.value = LibraryModes.SONGS
                    }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        verticalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        Text(
                            text = artistDRO.author.name,
                            color = Black0,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(20.dp, 0.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${stringResource(id = R.string.library_downloaded_songs)}: ${artistDRO.songsCount}",
                            color = Gold50,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(20.dp, 0.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
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