package ru.frozenpriest.tasks

import co.touchlab.kermit.Logger
import ru.frozenpriest.api.KomgaApi
import ru.frozenpriest.api.SuwayomiApi
import ru.frozenpriest.db.Database
import ru.frozenpriest.utils.getSeriesIdByPath

suspend fun updateMangaMetadata() {
    Logger.i { "Checking new metadata in suwayomi" }

    val komgaSeries = KomgaApi.listSeries()
    Database.getAll()
        .groupBy { seriesLink -> seriesLink.komgaPath }
        .mapValues { it.value.sortedByDescending { seriesLink -> seriesLink.priority } }
        .forEach { (komgaSeriesDir, seriesLinks) ->
            Logger.d { "Checking new metadata for manga: $komgaSeriesDir" }
            val suwayomiMetadata = seriesLinks.firstNotNullOfOrNull { link ->
                SuwayomiApi.getMangaMetadata(link.suwayomiId)
            }

            if (suwayomiMetadata != null) {
                komgaSeries.getSeriesIdByPath(komgaSeriesDir)?.let { seriesId ->
                    KomgaApi.updateSeriesMetadata(seriesId, suwayomiMetadata)
                }
                Logger.d { "Metadata updated in komga: $komgaSeriesDir" }
            }
        }
}
