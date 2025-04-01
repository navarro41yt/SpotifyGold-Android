package com.navarro.spotifygold.services.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import androidx.core.app.NotificationCompat
import com.navarro.spotifygold.MainActivity
import com.navarro.spotifygold.R
import com.navarro.spotifygold.entities.AudioDRO
import com.navarro.spotifygold.services.notification.activities.NextActivity
import com.navarro.spotifygold.services.notification.activities.PauseActivity
import com.navarro.spotifygold.services.notification.activities.PreviousActivity

private const val NOTIFICATION_ID = 101

fun createNotificationV1(
    context: Context,
    mediaPlayer: MediaPlayer,
    currentSong: AudioDRO,
    ongoing: Boolean = true
) {
    // Create a notification channel for Android O and above
    val channelId = "media_player_channel"
    val channelName = "Media Player"
    val importance = NotificationManager.IMPORTANCE_HIGH
    val channel = NotificationChannel(channelId, channelName, importance)
    val notificationManagerInstance =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManagerInstance.createNotificationChannel(channel)

    // Create an intent for the action when the notification is clicked
    val notificationIntent = Intent(context, MainActivity::class.java)
    val pendingIntent =
        PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

    // Create intents for notification actions
    val pauseIntent = Intent(context, PauseActivity::class.java)
    val previousIntent = Intent(context, PreviousActivity::class.java)
    val nextIntent = Intent(context, NextActivity::class.java)

    // Build the notification
    val notificationBuilder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_spotify_gold)
        .setContentTitle(currentSong.getSafeTitle())
        .setContentText(currentSong.getSafeArtist())
        .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_spotify_gold))
        .setContentIntent(pendingIntent)
        .setShowWhen(false)
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setOngoing(ongoing)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .addAction(
            R.drawable.ic_previous,
            "Previous",
            PendingIntent.getActivity(
                context,
                0,
                previousIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
        )
        .addAction(
            if (mediaPlayer.isPlaying) R.drawable.ic_pause else R.drawable.ic_play,
            "Pause",
            PendingIntent.getActivity(
                context,
                0,
                pauseIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
        )
        .addAction(
            R.drawable.ic_next,
            "Next",
            PendingIntent.getActivity(
                context,
                0,
                nextIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
        )

    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
}

fun deleteNotificationV1(context: Context) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.cancel(NOTIFICATION_ID)
}

fun deleteNotificationChannelV1(context: Context) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.deleteNotificationChannel("media_player_channel")
}

