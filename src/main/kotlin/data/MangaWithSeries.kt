package ru.frozenpriest.data

data class MangaWithSeries(
    val title: String,
    val sourceName: String,
    val sourceId: String,
    val suwayomiId: Int,
    val komgaPath: String?,
    val priority: Int = 1,
)