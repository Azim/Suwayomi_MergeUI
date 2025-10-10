package ru.frozenpriest

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.frozenpriest.environment.Environment
import ru.frozenpriest.tasks.fetchNewChapters
import ru.frozenpriest.tasks.updateMangaCovers
import ru.frozenpriest.tasks.updateMangaMetadata

private val coroutineScope = CoroutineScope(SupervisorJob())

fun main(): Unit = runBlocking {
    Logger.setMinSeverity(Environment.SEVERITY)

    coroutineScope.launch {
        startServer()
    }
    coroutineScope.launch {
        fetchNewChapters()
        updateMangaCovers()
        updateMangaMetadata()
    }

    launch {
        while (true) {
            delay(1000)
        }
    }
}