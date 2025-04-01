package com.navarro.spotifygold.components.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.navarro.spotifygold.components.global.PlayingGif
import com.navarro.spotifygold.entities.AudioDRO
import com.navarro.spotifygold.entities.metadata.MetadataEntity
import com.navarro.spotifygold.ui.theme.Black0
import com.navarro.spotifygold.ui.theme.Black50
import com.navarro.spotifygold.ui.theme.Gold50

@Composable
fun HomeItem(
    song: MetadataEntity,
    type: String,
    currentSong: MutableState<AudioDRO>,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(20.dp)
            .width(150.dp)
            .clickable {
                onClick()
            }
    ) {
        AsyncImage(
            model = song.thumbnail,
            contentDescription = "Song Thumbnail",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(150.dp)
                .clip(RoundedCornerShape(10.dp))
                .aspectRatio(1f)
        )
        Spacer(modifier = Modifier.size(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = song.author.name,
                    color = Gold50,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = song.title,
                    color = Black0,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = type,
                    color = Black50,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            if (currentSong.value.getSafeId() == song.id) {
                PlayingGif(
                    modifier = Modifier.size(30.dp)
                )
            }
        }

    }
}