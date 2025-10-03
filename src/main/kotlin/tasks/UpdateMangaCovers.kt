package ru.frozenpriest.tasks

import co.touchlab.kermit.Logger
import java.io.File
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes
import kotlin.io.path.copyTo
import kotlin.io.path.exists
import kotlin.io.path.extension
import kotlin.io.path.notExists
import kotlin.io.path.readAttributes
import kotlin.io.path.useDirectoryEntries
import kotlin.time.Clock
import kotlin.time.Instant
import ru.frozenpriest.api.KomgaApi
import ru.frozenpriest.data.KomgaSeries
import ru.frozenpriest.db.Database
import ru.frozenpriest.environment.Environment

suspend fun updateMangaCovers(clock: Clock = Clock.System) {
    Logger.i { "Checking new thumbnails in suwayomi" }
    val thumbnailsFolder = Paths.get(Environment.SUWAYOMI_LIBRARY_PATH, "thumbnails")
    if (thumbnailsFolder.notExists()) return

    val komgaSeries = KomgaApi.listSeries()
    Database.getAll()
        .groupBy { seriesLink -> seriesLink.komgaPath }
        .mapValues { it.value.sortedByDescending { seriesLink -> seriesLink.priority } }
        .forEach { (komgaSeriesDir, seriesLinks) ->
            Logger.d { "Checking new thumbnails for manga: $komgaSeriesDir" }
            val suwayomiThumbnailPath = seriesLinks.firstNotNullOfOrNull { link ->
                thumbnailsFolder.useDirectoryEntries("${link.suwayomiId}.*") { paths ->
                    paths.firstOrNull()
                }
            }

            if (suwayomiThumbnailPath != null) {
                Logger.d { "Found new thumbnail: $suwayomiThumbnailPath" }
                val komgaPath = Paths.get(
                    Environment.KOMGA_LIBRARY_PATH,
                    komgaSeriesDir,
                    "cover.${suwayomiThumbnailPath.extension}"
                )

                if (komgaPath.exists()) {
                    val komgaAttributes = komgaPath.readAttributes<BasicFileAttributes>()

                    val creationInstant = Instant.fromEpochMilliseconds(komgaAttributes.creationTime().toMillis())
                    if (clock.now() - creationInstant <= Environment.COVER_LIFETIME) {
                        Logger.d { "Thumbnail is not old, skipping" }
                        return@forEach
                    }
                }

                suwayomiThumbnailPath.copyTo(komgaPath, overwrite = true)
                komgaSeries.getSeriesIdByPath(komgaSeriesDir)?.let { seriesId ->
                    KomgaApi.refreshSeriesMetadata(seriesId)
                }
                Logger.d { "Thumbnail updated in komga: $komgaPath" }
            }
        }
}

private fun List<KomgaSeries>.getSeriesIdByPath(path: String): String? = firstOrNull { series ->
    series.url.split(File.separator).last() == path
}?.id
