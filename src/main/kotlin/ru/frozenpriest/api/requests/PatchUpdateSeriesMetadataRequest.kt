package ru.frozenpriest.api.requests

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable
import ru.frozenpriest.data.MangaMetadata
import ru.frozenpriest.data.MangaStatus

@Resource("/api/v1/series/{seriesId}/metadata")
data class PatchUpdateSeriesMetadataRequest(
    val seriesId: String,
) {
    @Serializable
    data class Body(
        val title: String,
        val summary: String?,
        val genres: List<String>,
        val status: MangaStatus?,
    ) {
        constructor(seriesMetadata: MangaMetadata) : this(
            title = seriesMetadata.title,
            summary = seriesMetadata.description,
            genres = seriesMetadata.genre,
            status = seriesMetadata.status,
        )
    }
}
