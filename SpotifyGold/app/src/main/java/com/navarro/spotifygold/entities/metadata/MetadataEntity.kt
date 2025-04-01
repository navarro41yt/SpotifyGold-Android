package com.navarro.spotifygold.entities.metadata

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.Date

@Entity
@Serializable
data class MetadataEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    @Embedded(prefix = "author_") val author: AuthorEntity,
    val uploadAt: String,
    val duration: Long,
    @Embedded(prefix = "engagement_") val engagement: EngagementEntity,
    var thumbnail: String = "",
    var played: Long = 0,
) {
    fun getLink(): String {
        return "https://www.youtube.com/watch?v=$id"
    }
}
