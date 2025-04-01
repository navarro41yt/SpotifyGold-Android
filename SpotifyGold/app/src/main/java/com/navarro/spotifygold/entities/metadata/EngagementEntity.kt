package com.navarro.spotifygold.entities.metadata

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Entity
@Serializable
data class EngagementEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = Random(1000).nextLong(),
    val views: Long,
    val likes: Long
)
