package ru.frozenpriest.api.requests

import io.ktor.resources.Resource

@Resource("/api/v1/libraries/{libraryId}/scan")
data class PostScanLibraryRequest(val libraryId: String)
