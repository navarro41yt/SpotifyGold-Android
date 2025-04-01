package com.navarro.spotifygold.entities.metadata

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    foreignKeys = [ForeignKey(
        entity = MetadataEntity::class,
        parentColumns = ["id"],
        childColumns = ["metadataId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ThumbnailEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val url: String,
    val size: Long,
    val metadataId: String
)
