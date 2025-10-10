package ru.frozenpriest.api.responses

import kotlinx.serialization.Serializable
import ru.frozenpriest.data.KomgaSeries

@Serializable
data class PostListSeriesResponse(
    val content: List<KomgaSeries>,
)
