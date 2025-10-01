package ru.frozenpriest.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.util.Properties
import ru.frozenpriest.Database
import ru.frozenpriest.data.MangaSeriesLink

object Database {
    private val driver: SqlDriver by lazy {
        JdbcSqliteDriver("jdbc:sqlite:config/manga.db", Properties(), Database.Schema)
    }

    private val instance by lazy {
        Database(driver)
    }

    fun insertPath(suwayomiId: Int, komgaPath: String) {
        instance.mangaQueries.insertOrReplace(suwayomiId.toLong(), komgaPath)
    }

    fun getKomgaSeriesDir(suwayomiId: Int): String? {
        return instance.mangaQueries.getKomgaPath(suwayomiId.toLong()).executeAsOneOrNull()
    }

    fun getAll(): List<MangaSeriesLink> {
        return instance.mangaQueries.getAll().executeAsList().map {
            MangaSeriesLink(
                suwayomiId = it.suwayomi_id.toInt(),
                komgaPath = it.komga_path,
                priority = 1,
            )
        }
    }

    fun remove(suwayomiId: Int) {
        instance.mangaQueries.remove(suwayomiId.toLong())
    }
}