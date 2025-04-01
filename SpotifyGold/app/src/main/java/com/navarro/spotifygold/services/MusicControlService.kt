package com.navarro.spotifygold.services

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat

class MusicControlService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            PlaybackAction.PLAY.name -> {
                // Play the music
            }
            PlaybackAction.PAUSE.name -> {
                // Pause the music
            }
            PlaybackAction.NEXT.name -> {
                // Play the next song
            }
            PlaybackAction.PREVIOUS.name -> {
                // Play the previous song
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun playMusic() {
        val notification = NotificationCompat.Builder(this, "media_player_channel")
    }

    enum class PlaybackAction {
        PLAY,
        PAUSE,
        NEXT,
        PREVIOUS
    }
}