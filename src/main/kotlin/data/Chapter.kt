package ru.frozenpriest.data

import java.nio.file.Path
import java.nio.file.Paths
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

private const val MANGA_EXTENSION = ".cbz"

fun Chapter.chapterFileName(): String {
    val chapterName = listOfNotNull(scanlator, name).joinToString(separator = "_").toValidFilename()
    return "$chapterName$MANGA_EXTENSION"
}

fun Chapter.suwayomiFilePath(): Path {
    val sourceDir = sourceName.toValidFilename()
    val mangaDir = mangaName.toValidFilename()
    return Paths.get(Environment.SUWAYOMI_LIBRARY_PATH, "mangas", sourceDir, mangaDir, chapterFileName())
}

fun Chapter.komgaFilePath(komgaSeriesDir: String): Path {
    val komgaChapterName = "${sourceName}_${chapterFileName()}".toValidFilename()
    return Paths.get(Environment.KOMGA_LIBRARY_PATH, komgaSeriesDir, komgaChapterName)
}
