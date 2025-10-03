package ru.frozenpriest.api.requests

import io.ktor.resources.Resource

@Resource("api/v1/series/{seriesId}/metadata/refresh")
data class PostRefreshSeriesMetadataRequest(
    val seriesId: String,
)