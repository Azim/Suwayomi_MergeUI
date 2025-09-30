package ru.frozenpriest.tasks

import co.touchlab.kermit.Logger
import java.nio.file.Files
import java.nio.file.Path
import ru.frozenpriest.api.KomgaApi
import ru.frozenpriest.api.SuwayomiApi
import ru.frozenpriest.data.Chapter
import ru.frozenpriest.data.komgaFilePath
import ru.frozenpriest.data.suwayomiFilePath
import ru.frozenpriest.db.Database
import ru.frozenpriest.environment.Environment
import ru.frozenpriest.utils.runCatching

suspend fun fetchNewChapters() = runCatching {
    val chapters = SuwayomiApi.fetchUnreadDownloadedChapters()
    Logger.i { "Checking new chapters in suwayomi: ${chapters.size} new chapters found" }
    val chaptersWithKomga = chapters.mapNotNull { chapter ->
        chapter.getFullKomgaFilePath()?.let { path -> chapter to path }
    }

    chaptersWithKomga.forEach { (chapter, komgaFilePath) ->
        val suwayomiFilePath = chapter.suwayomiFilePath()
        Logger.d { "Moving chapter from $suwayomiFilePath to $komgaFilePath" }
        Files.move(suwayomiFilePath, komgaFilePath)

        SuwayomiApi.markChapterRead(chapterId = chapter.chapterId)
    }
    Logger.i { "Chapter update finished: ${chaptersWithKomga.count()} new chapters" }

    val libraryId = KomgaApi.getAllLibraries().first { it.name == Environment.KOMGA_LIBRARY_NAME }.id
    KomgaApi.rescanLibrary(libraryId)
    Logger.i { "Komga rescan requested" }
}

private fun Chapter.getFullKomgaFilePath(): Path? {
    val seriesDir = Database.getKomgaSeriesDir(suwayomiId = mangaId) ?: return null
    return komgaFilePath(komgaSeriesDir = seriesDir)
}