package com.navarro.spotifygold.services.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.navarro.spotifygold.entities.metadata.AuthorEntity
import com.navarro.spotifygold.entities.metadata.EngagementEntity
import com.navarro.spotifygold.entities.metadata.MetadataEntity
import com.navarro.spotifygold.entities.metadata.MostPlayedEntity
import com.navarro.spotifygold.services.repository.MetadataRepository

@Database(
    entities = [
        MetadataEntity::class,
        EngagementEntity::class,
        AuthorEntity::class,
        MostPlayedEntity::class
    ],
    version = 2
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun metadataRepo(): MetadataRepository
}
