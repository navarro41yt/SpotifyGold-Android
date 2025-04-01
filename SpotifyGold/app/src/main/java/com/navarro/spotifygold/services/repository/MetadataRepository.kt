package com.navarro.spotifygold.services.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.navarro.spotifygold.entities.metadata.AuthorEntity
import com.navarro.spotifygold.entities.metadata.MetadataEntity

@Dao
interface MetadataRepository {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMetadata(metadata: MetadataEntity)

    @Transaction
    @Query("DELETE FROM MetadataEntity WHERE id = :id")
    fun deleteMetadata(id: String)

    @Query("SELECT * FROM MetadataEntity WHERE id = :id")
    fun getMetadata(id: String): MetadataEntity

    @Query("SELECT * FROM MetadataEntity")
    fun getAll(): List<MetadataEntity>

    @Query("SELECT DISTINCT author_id AS id, author_name AS name FROM MetadataEntity")
    fun getAuthors(): List<AuthorEntity>

    @Query("SELECT id FROM MetadataEntity WHERE author_id = :authorId")
    fun getIdsByAuthorId(authorId: String): List<String>

    @Query("SELECT * FROM MetadataEntity ORDER BY played DESC LIMIT 8")
    fun getMostPlayed(): List<MetadataEntity>

    @Query("SELECT * FROM MetadataEntity ORDER BY played ASC LIMIT 8")
    fun getLeastPlayed(): List<MetadataEntity>

    @Query("SELECT * FROM MetadataEntity WHERE duration > ${1.5f * 60 * 60}")
    fun getPodcasts(): List<MetadataEntity>

    fun getRandom(size: Int): List<MetadataEntity> {
        return getAll().shuffled().take(size)
    }
}