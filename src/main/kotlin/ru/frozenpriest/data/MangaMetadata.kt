package ru.frozenpriest.data


data class MangaMetadata(
    val title: String,
    val description: String?,
    val genre: List<String>,
    val status: MangaStatus?,
)
