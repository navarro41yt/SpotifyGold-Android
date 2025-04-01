package com.navarro.spotifygold.entities.metadata

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class AuthorEntity(
    @PrimaryKey
    val id: String,
    val name: String,
) {
    @Ignore
    var url: String = "https://www.youtube.com/channel/$id"
}
