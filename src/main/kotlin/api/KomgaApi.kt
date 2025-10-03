package ru.frozenpriest.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.java.Java
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import ru.frozenpriest.api.requests.GetListAllLibrariesRequest
import ru.frozenpriest.api.requests.PostListSeriesRequest
import ru.frozenpriest.api.requests.PostRefreshSeriesMetadataRequest
import ru.frozenpriest.api.requests.PostScanLibraryRequest
import ru.frozenpriest.api.responses.PostListSeriesResponse
import ru.frozenpriest.data.KomgaSeries
import ru.frozenpriest.data.Library
import ru.frozenpriest.environment.Environment
import co.touchlab.kermit.Logger as Kermit

data object KomgaApi {
    private val client = HttpClient(Java) {
        install(Resources)
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }
            )
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Kermit.d { message }
                }
            }
            level = LogLevel.INFO
        }
        install(HttpTimeout)

        defaultRequest {
            url(Environment.KOMGA_URL)
        }
    }.apply {
        plugin(HttpSend).intercept { request ->
            request.headers.append("X-API-Key", Environment.KOMGA_API_KEY)
            execute(request)
        }
    }

    suspend fun getAllLibraries(): List<Library> {
        return client.get(GetListAllLibrariesRequest).body()
    }

    suspend fun rescanLibrary(id: String) {
        client.post(PostScanLibraryRequest(libraryId = id))
    }

    suspend fun listSeries(): List<KomgaSeries> {
        return client.post(PostListSeriesRequest()) {
            contentType(ContentType.Application.Json)
            setBody("{}")
        }.body<PostListSeriesResponse>().content.map {
            KomgaSeries(id = it.id, url = it.url)
        }
    }

    suspend fun refreshSeriesMetadata(seriesId: String) {
        client.post(PostRefreshSeriesMetadataRequest(seriesId))
    }
}
