package ru.frozenpriest

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.frozenpriest.tasks.fetchNewChapters
import ru.frozenpriest.tasks.updateMangaMetadata

private val coroutineScope = CoroutineScope(SupervisorJob())

fun main(): Unit = runBlocking {
    coroutineScope.launch {
        startServer()
    }
    coroutineScope.launch {
        fetchNewChapters().onFailure {
            Logger.e(it) { "Failed to fetch new chapters" }
            throw it
        }
        updateMangaMetadata()
    }

    launch {
        while (true) {
            delay(1000)
        }
    }
}