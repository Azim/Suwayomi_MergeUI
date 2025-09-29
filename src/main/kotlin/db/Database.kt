package ru.frozenpriest.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import ru.frozenpriest.Database

object Database {
    private val driver: SqlDriver by lazy {
        JdbcSqliteDriver("jdbc:sqlite:manga.db").apply {
            Database.Schema.create(this)
        }
    }

    private val instance by lazy {
        Database(driver)
    }

    fun insertPath(suwayomiId: Int, komgaPath: String) {
        instance.mangaQueries.insertOrReplace(suwayomiId.toLong(), komgaPath)
    }

    fun getKomgaPath(suwayomiId: Int): String {
        return instance.mangaQueries.getKomgaPath(suwayomiId.toLong()).executeAsOne()
    }
}