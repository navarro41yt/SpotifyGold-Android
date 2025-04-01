package com.navarro.spotifygold.entities

import kotlinx.serialization.Serializable

@Serializable
data class DtoLogIn (
    val username: String,
    val password: String,
    val email: String
)
