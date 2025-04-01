package com.navarro.spotifygold.components.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.navarro.spotifygold.components.global.IconInfo
import com.navarro.spotifygold.entities.DtoResultEntity
import com.navarro.spotifygold.utils.formatLikes
import com.navarro.spotifygold.utils.formatTime
import com.navarro.spotifygold.utils.formatTimeSeconds
import com.navarro.spotifygold.utils.formatViews

@Composable
fun QueryResultItem(
    audioInfo: DtoResultEntity, onDownloadClick: (DtoResultEntity) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .clickable {
                onDownloadClick(audioInfo)
            }) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
        ) {
            Image(
                painter = rememberAsyncImagePainter(audioInfo.thumbnail),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth(0.73f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = audioInfo.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            // Row | Arrange Space between
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${formatTimeSeconds(audioInfo.duration)}・${audioInfo.authorName}",
                    color = Color.LightGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

        }
        Column(
            // Center the icons
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            IconInfo(
                text = formatViews(audioInfo.views),
                icon = Icons.Default.RemoveRedEye
            )
            IconInfo(
                text = formatLikes(audioInfo.likes),
                icon = Icons.Default.ThumbUp
            )
        }
    }
}
