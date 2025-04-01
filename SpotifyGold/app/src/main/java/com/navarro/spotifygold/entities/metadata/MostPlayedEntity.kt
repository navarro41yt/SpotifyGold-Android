package com.navarro.spotifygold.entities.metadata

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(
    indices = [Index(value = ["file"], unique = true)]
)
@Serializable
data class MostPlayedEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val file: String,
    val timesPlayed: Int
)
