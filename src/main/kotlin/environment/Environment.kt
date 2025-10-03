package ru.frozenpriest.environment

import co.touchlab.kermit.Severity
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

object Environment {

    const val SUWAYOMI_LIBRARY_PATH = "/suwayomi"
    const val KOMGA_LIBRARY_PATH = "/komga"

    val SUWAYOMI_URL by lazy {
        requireEnv("SUWAYOMI_URL")
    }

    val KOMGA_URL by lazy {
        requireEnv("KOMGA_URL")
    }

    val KOMGA_API_KEY by lazy {
        requireEnv("KOMGA_API_KEY")
    }

    val KOMGA_LIBRARY_NAME by lazy {
        requireEnv("KOMGA_LIBRARY_NAME")
    }

    val COVER_LIFETIME: Duration by lazy {
        System.getenv("COVER_LIFETIME_DAYS")?.toIntOrNull()?.days ?: 7.days
    }

    val SEVERITY by lazy {
        System.getenv("LOG_LEVEL")?.toSeverityOrNull() ?: Severity.Info
    }
}

private fun requireEnv(name: String) = requireNotNull(System.getenv(name)) {
    "$name is null"
}

private fun String.toSeverityOrNull(): Severity? {
    return when (this) {
        "VERBOSE" -> Severity.Verbose
        "DEBUG" -> Severity.Debug
        "INFO" -> Severity.Info
        "WARN" -> Severity.Warn
        "ERROR" -> Severity.Error
        "ASSERT" -> Severity.Assert
        else -> null
    }
}