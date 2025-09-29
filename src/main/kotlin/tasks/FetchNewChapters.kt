package ru.frozenpriest.tasks

import co.touchlab.kermit.Logger
import ru.frozenpriest.SuwayomiApi
import ru.frozenpriest.data.suwayomiFilePath

suspend fun fetchNewChapters() {
    val chapters = SuwayomiApi.fetchUnreadDownloadedChapters()
    Logger.i { "Checking new chapters in suwayomi: ${chapters.size} new chapters found" }
    chapters.filter {
        true // TODO: check if we have path for it in komga
    }.forEach { chapter ->
        val suwayomiFilePath = chapter.suwayomiFilePath()
        // find path in komga (find folder for komga, if null -> skip)
        // move chapter to komga
        // mark chapter as read in suwayomi
    }
    // launch rescan in komga
}