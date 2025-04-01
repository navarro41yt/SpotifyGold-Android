package com.navarro.spotifygold.views

import RequestStoragePermission
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.navarro.spotifygold.R
import com.navarro.spotifygold.services.StaticToast
import com.navarro.spotifygold.components.search.QueryResultItem
import com.navarro.spotifygold.components.search.SearchBar
import com.navarro.spotifygold.models.SearchCallBack
import com.navarro.spotifygold.services.downloadSong
import com.navarro.spotifygold.services.search
import com.navarro.spotifygold.entities.DtoResultEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SearchScreen() {
    val context = LocalContext.current

    val query = remember { mutableStateOf("") }
    val audioList = remember { mutableStateListOf<DtoResultEntity>() }
    var currentAudioInfo by remember { mutableStateOf<DtoResultEntity?>(null) }
    var requestPermission by remember { mutableStateOf(false) }
    val isTextBoxFocused = remember { mutableStateOf(false) }
    var hasSearched by remember { mutableStateOf(false) }

    val coroutineScope = CoroutineScope(Dispatchers.IO)

    if (requestPermission) {
        currentAudioInfo?.let { audioInfo ->
            RequestStoragePermission { isGranted ->
                if (isGranted) {
                    coroutineScope.launch {
                        downloadSong(context, audioInfo, 0)
                    }
                } else {
                    // Handle permission denied case
                    StaticToast.showToast("Permission denied, activate it through app settings")
                }
                requestPermission = false
            }
        }
    }

    if (query.value.isEmpty() && hasSearched) {
        audioList.clear()
        hasSearched = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        if (!isTextBoxFocused.value) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
            ) {
                AsyncImage(
                    model = "https://spotifygold.azurewebsites.net/favicon.ico",
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .fillMaxWidth(0.2f)
                        .clip(CircleShape)
                )
                Text(
                    text = stringResource(id = R.string.navigation_search),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
        ) {
            SearchBar(
                query = query,
                isFocused = isTextBoxFocused,
            ) {
                search(
                    context,
                    query.value,
                    object : SearchCallBack {
                    override fun onSuccess(newAudioList: List<DtoResultEntity>) {
                        audioList.clear()

                        for (audio in newAudioList) {
                            audioList.add(audio)
                        }

                        hasSearched = true
                    }

                    override fun onFailure(error: String) {
                        Log.e("SearchScreen", "Error: $error")
                    }
                })
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(count = audioList.size, key = { index -> audioList[index].id }) { index ->
                QueryResultItem(audioInfo = audioList[index], onDownloadClick = { audioInfo ->
                    currentAudioInfo = audioInfo
                    requestPermission = true
                })
            }
            item {
                Spacer(modifier = Modifier.height(200.dp))
            }
        }
        Box(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        )
    }
}
