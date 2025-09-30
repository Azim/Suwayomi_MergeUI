package ru.frozenpriest.data

import kotlinx.serialization.Serializable

@Serializable
data class Library(
    val id: String,
    val name: String,
)
