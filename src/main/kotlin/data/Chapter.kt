package ru.frozenpriest.data

import ru.frozenpriest.environment.Environment
import ru.frozenpriest.utils.toValidFilename

data class Chapter(
    val chapterId: Int,
    val name: String,
    val scanlator: String?,
    val mangaId: Int,
    val mangaName: String,
    val sourceId: String,
    val sourceName: String,
)

fun Chapter.suwayomiFilePath(): String = buildString {
    val sourceDir = sourceName.toValidFilename()
    val mangaDir = mangaName.toValidFilename()
    return "${Environment.SUWAYOMI_LIBRARY_PATH}/$sourceDir/$mangaDir"
}
