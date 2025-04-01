package com.navarro.spotifygold.services.room

import androidx.room.TypeConverter
import com.navarro.spotifygold.entities.metadata.AuthorEntity
import com.navarro.spotifygold.entities.metadata.EngagementEntity
import java.time.OffsetDateTime
import java.time.Duration
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.Date

class Converters {
    @TypeConverter
    fun fromOffsetDateTime(value: OffsetDateTime?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toOffsetDateTime(value: String?): OffsetDateTime? {
        return value?.let { OffsetDateTime.parse(it) }
    }

    @TypeConverter
    fun fromDuration(value: Duration?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toDuration(value: String?): Duration? {
        return value?.let { Duration.parse(it) }
    }

    // Serialize and deserialize DtoAuthorResponse and DtoEngagementResponse
    @TypeConverter
    fun fromAuthorResponse(value: AuthorEntity): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toAuthorResponse(value: String): AuthorEntity {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromEngagementResponse(value: EngagementEntity): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toEngagementResponse(value: String): EngagementEntity {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
