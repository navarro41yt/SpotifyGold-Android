package com.navarro.spotifygold.entities

import androidx.compose.runtime.Immutable
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Immutable
@Serializable
data class DtoResultEntity(
    val id: String,
    val title: String,
    val authorName: String,
    val thumbnail: String,
    val duration: Long,
    val views: Long,
    val likes: Long,
)
