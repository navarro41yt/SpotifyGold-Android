package com.navarro.spotifygold.entities

import com.navarro.spotifygold.entities.metadata.AuthorEntity

class ArtistDRO(
    var author: AuthorEntity,
    var songsCount: Int = 0
)
