package ru.frozenpriest.api.requests

import io.ktor.resources.Resource

@Resource("api/v1/series/list")
class PostListSeriesRequest(
    val unpaged: Boolean = true,
)
