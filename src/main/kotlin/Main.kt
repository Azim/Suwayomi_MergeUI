package ru.frozenpriest

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
        fetchNewChapters()
        updateMangaMetadata()
    }

    launch {
        while (true) { delay(1000) }
    }
}