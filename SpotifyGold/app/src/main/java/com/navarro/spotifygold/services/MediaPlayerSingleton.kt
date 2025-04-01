package com.navarro.spotifygold.services

import android.content.Context
import androidx.annotation.OptIn
import androidx.compose.runtime.MutableState
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import com.navarro.spotifygold.entities.AudioDRO
import com.navarro.spotifygold.services.room.DatabaseProvider
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private enum class MediaNavigationModes(val value: Int) {
    NEXT(1),
    PREVIOUS(-1)
}

object MediaPlayerSingleton {

    lateinit var mediaPlayer: ExoPlayer
    lateinit var mediaSession: MediaSession

    /**
     * Play or pause the current media
     */
    fun playPause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        } else {
            mediaPlayer.play()
        }
    }

    /**
     * Play the current media
     */
    fun play() {
        mediaPlayer.play()
    }

    /**
     * Play a new media
     * @param audioDRO Audio to be Played
     */
    @OptIn(UnstableApi::class)
    @kotlin.OptIn(DelicateCoroutinesApi::class)
    fun play(
        context: Context,
        audioDRO: AudioDRO
    ) {
        val uri = audioDRO.route

        val mediaMetadata: MediaMetadata = MediaMetadata.Builder()
            .setTitle(audioDRO.getSafeTitle())
            .setArtist(audioDRO.getSafeArtist())
            .build()
        val mediaItem = MediaItem.Builder()
            .setUri(uri)
            .setMediaMetadata(mediaMetadata)
            .build()
        mediaPlayer.setMediaItem(mediaItem)
        mediaPlayer.prepare()
        mediaPlayer.play()

        GlobalScope.launch {
            incrementPlayedTimes(context, audioDRO)
        }
    }

    /**
     * Pause the current media
     */
    fun pause() {
        mediaPlayer.pause()
    }

    /**
     * Play the next media in the queue
     * @param queue List of media
     * @param current Current media
     */
    fun next(
        context: Context,
        queue: MutableList<AudioDRO>, current: MutableState<AudioDRO>
    ) {
        navigate(
            context,
            queue, current, MediaNavigationModes.NEXT.value
        )
    }

    /**
     * Play the previous media in the queue
     * @param queue List of media
     * @param current Current media
     */
    fun previous(
        context: Context,
        queue: MutableList<AudioDRO>, current: MutableState<AudioDRO>
    ) {
        navigate(
            context,
            queue, current, MediaNavigationModes.PREVIOUS.value
        )
    }

    /**
     * Navigate through the queue
     * @param queue List of media
     * @param current Current media
     * @param step Step to navigate
     */
    private fun navigate(
        context: Context,
        queue: MutableList<AudioDRO>,
        current: MutableState<AudioDRO>,
        step: Int
    ) {
        when {
            SettingsSingleton.loop.value -> {}
            else -> {
                val currentIndex = queue.indexOf(current.value)
                val newIndex = (currentIndex + step).let {
                    when {
                        it >= queue.size -> 0
                        it < 0 -> queue.size - 1
                        else -> it
                    }
                }
                current.value = queue[newIndex]
            }
        }
        play(
            context,
            current.value
        )
    }

    /**
     * Get a random media from the queue
     * @param queue List of media
     * @param current Current media
     * @return Random media
     */
    @Deprecated("Used .shuffle of Lists instead")
    private fun random(queue: MutableList<AudioDRO>, current: AudioDRO): AudioDRO {
        var newValue = current
        while (newValue == current) {
            newValue = queue.random()
        }
        return newValue
    }

    private fun incrementPlayedTimes(
        context: Context,
        audioDRO: AudioDRO
    ) {
        val db = DatabaseProvider.getDatabase(context)
        if (audioDRO.metadata != null) {
            val metadata = db.metadataRepo().getMetadata(audioDRO.metadata!!.id)
            metadata.played++
            db.metadataRepo().insertMetadata(metadata)
        }
    }
}