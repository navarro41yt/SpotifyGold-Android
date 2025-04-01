package com.navarro.spotifygold.services

import android.content.Context
import android.os.Environment
import android.util.Log
import com.navarro.spotifygold.R
import com.navarro.spotifygold.models.SearchCallBack
import com.navarro.spotifygold.entities.ArtistDRO
import com.navarro.spotifygold.entities.AudioDRO
import com.navarro.spotifygold.entities.DtoResultEntity
import com.navarro.spotifygold.entities.metadata.AuthorEntity
import com.navarro.spotifygold.entities.metadata.MetadataEntity
import com.navarro.spotifygold.services.room.AppDatabase
import com.navarro.spotifygold.services.room.DatabaseProvider
import com.navarro.spotifygold.utils.Constants
import com.navarro.spotifygold.utils.FileUtils
import com.navarro.spotifygold.utils.hasManageExternalStoragePermission
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default.decodeFromString
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.UnknownServiceException

private const val localUrl = "${Constants.url}yt/"

fun readMusic(
    metadata: MetadataEntity
): AudioDRO {
    val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
    val file = File(storageDir, "${Constants.prefix}${FileUtils.sanitizeString(metadata.id)}.mp3")

    return AudioDRO(metadata, file.absolutePath, 0)
}

fun readMusicFolder(
    context: Context,
    artist: AuthorEntity? = null
): List<AudioDRO> {
    Log.d("ReadMusicFolder", "Reading music folder")
    val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
    val files = storageDir.listFiles()
    val listAudioDRO = mutableListOf<AudioDRO>()

    val db = DatabaseProvider.getDatabase(context)

    var pos = 0

    // Get just the .mp3 files
    files?.filter {
        it.name.endsWith(".mp3")
    }?.forEach {
        val isSPGAudio = it.name.startsWith(Constants.prefix)
        var metadata: MetadataEntity? = null

        if (isSPGAudio) {
            val id = it.name.substringAfter(Constants.prefix).substringBefore(".mp3")
            metadata = db.metadataRepo().getMetadata(id)
        }

        val newAudioDRO = AudioDRO(metadata, it.absolutePath, pos)

        if (artist == null) {
            listAudioDRO.add(newAudioDRO)
        } else {
            if (metadata!!.author.id == artist.id) {
                listAudioDRO.add(newAudioDRO)
            }
        }

        pos++
    }

    Log.d("ReadMusicFolder", "Found ${listAudioDRO.size} audio files")
    return listAudioDRO
}

fun readArtists(context: Context): List<ArtistDRO> {
    val db = DatabaseProvider.getDatabase(context)
    val authors = db.metadataRepo().getAuthors()
    val listArtistDRO = mutableListOf<ArtistDRO>()

    // Get the directory for music files
    val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
    val files = storageDir?.listFiles()?.filter { it.name.endsWith(".mp3") } ?: emptyList()

    // Process each author
    authors.forEach { authorEntity ->
        val authorId = authorEntity.id
        val listIds = db.metadataRepo().getIdsByAuthorId(authorId).toMutableList()
        var count = 0

        // Process each id in listIds
        listIds.toList().forEach { idToCheck ->
            // Filter files to those matching the prefix and id
            files.filter { file ->
                val isSPGAudio = file.name.startsWith(Constants.prefix)
                val id = file.name.substringAfter(Constants.prefix).substringBefore(".mp3")
                isSPGAudio && id == idToCheck
            }.forEach { file ->
                // Increment count and remove the matched id from listIds
                count++
                listIds.remove(idToCheck)
            }
        }

        // Add the processed author and count to the listArtistDRO
        if (count > 0)
            listArtistDRO.add(ArtistDRO(authorEntity, count))
    }

    return listArtistDRO
}


suspend fun downloadSong(
    context: Context,
    audioInfo: DtoResultEntity,
    mode: Int
) {
    Log.d("Download", "Downloading ${audioInfo.title}")
    val id = audioInfo.id
    val url = "${localUrl}$id/download?quality=$mode"

    if (!hasManageExternalStoragePermission(context)) {
        Log.e("DownloadError", "Permission denied")
    }

    withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        try {
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val fileName = "${Constants.prefix}${FileUtils.sanitizeString(id)}.mp3"
                response.body?.byteStream()?.let { inputStream ->
                    saveFileToStorage(context, inputStream, fileName, audioInfo)
                }
            } else {
                Log.e("DownloadError", "Error: ${response.code}")
            }
        } catch (e: IOException) {
            Log.e("DownloadError", "Error downloading file: ${e.message}")
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun search(
    context: Context,
    query: String,
    callback: SearchCallBack
) {
    GlobalScope.launch(Dispatchers.IO) {
        val timeStart = System.currentTimeMillis()
        try {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("${localUrl}search?query=$query&maxResults=15")
                .build()

            val response: Response
            try {
                response = client.newCall(request).execute()
            } catch (e: UnknownServiceException) {
                StaticToast.showToast(context.getString(R.string.error_server_unavailable))
                e.printStackTrace()
                return@launch
            }
            val responseData = response.body?.string()

            // Parse the response, with the help of kotlinx.serialization
            if (response.isSuccessful) {
                var audioList = emptyList<DtoResultEntity>()
                try {
                    audioList = decodeFromString(responseData!!)
                } catch (e: Exception) {
                    Log.e("SearchScreen", "Error parsing response: ${e.message}", e)
                }
                withContext(Dispatchers.Main) {
                    callback.onSuccess(audioList)
                }
            } else {
                withContext(Dispatchers.Main) {
                    callback.onFailure("Error: ${response.code} - ${response.message}")
                }
            }
        } catch (e: IOException) {
            Log.e("SearchScreen", "Error during network call: ${e.message}", e)
            // Handle error (e.g., show error message)
        }
        Log.d("SearchScreen", "Time taken: ${System.currentTimeMillis() - timeStart}ms")
    }
}

fun saveFileToStorage(
    context: Context,
    inputStream: InputStream,
    fileName: String,
    audioInfo: DtoResultEntity
) {
    val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
    val file = File(storageDir, fileName)

    Log.d("Download", "Saving to ${file.absolutePath}")
    FileOutputStream(file).use { outputStream ->
        val buffer = ByteArray(4096)
        var bytesRead: Int
        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            outputStream.write(buffer, 0, bytesRead)
        }
    }
    StaticToast.showToast(context.getString(R.string.download_saved_successfully))
    Log.d("Download", "File Successfully Downloaded!!!")
    inputStream.close()

    getInfo(context, audioInfo.id)
}

@OptIn(DelicateCoroutinesApi::class)
fun getInfo(
    context: Context,
    id: String
) {
    val db: AppDatabase = DatabaseProvider.getDatabase(context)

    // Update the metadata
    GlobalScope.launch(Dispatchers.IO) {
        try {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("${localUrl}$id/info")
                .build()

            Log.d("Download", "Fetching metadata for ${request.url}")

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val responseData = response.body?.string()
                val json = Json { ignoreUnknownKeys = true }
                val metadata = json.decodeFromString<MetadataEntity>(responseData!!)

                // Store in RoomDB
                db.metadataRepo().insertMetadata(metadata)
            } else {
                Log.e("Download", "Error fetching metadata: ${response.code}")
            }
        } catch (e: IOException) {
            Log.e("Download", "Error updating metadata: ${e.message}")
        }
    }
}

fun getAllInfo(
    context: Context,
    ids: List<String>
) {
    ids.forEach {
        getInfo(context, it)
    }
}
