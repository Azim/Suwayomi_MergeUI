package ru.frozenpriest.data

import kotlinx.serialization.Serializable

@Serializable
data class KomgaSeries(
    val id: String,
    val url: String,
)
