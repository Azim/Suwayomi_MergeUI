package ru.frozenpriest.api

import com.expediagroup.graphql.client.ktor.GraphQLKtorClient
import com.expediagroup.graphql.client.serialization.GraphQLClientKotlinxSerializer
import java.net.URI
import ru.frozenpriest.data.Chapter
import ru.frozenpriest.data.Manga
import ru.frozenpriest.environment.Environment
import ru.frozenpriest.generated.GetManga
import ru.frozenpriest.generated.MarkChapterRead
import ru.frozenpriest.generated.NewMangaChapters

data object SuwayomiApi {
    private val client = GraphQLKtorClient(
        url = URI.create("${Environment.SUWAYOMI_URL}/api/graphql").toURL(),
        serializer = GraphQLClientKotlinxSerializer(),
    )

    suspend fun fetchUnreadDownloadedChapters(): List<Chapter> {
        val chapters = client.execute(NewMangaChapters())
        return requireNotNull(chapters.data).chapters.nodes.map {
            val source = requireNotNull(it.manga.source) { "source name is null" }
            Chapter(
                chapterId = it.id,
                name = it.name,
                scanlator = it.scanlator,
                mangaId = it.manga.id,
                mangaName = it.manga.title,
                sourceId = source.id,
                sourceName = source.displayName,
            )
        }
    }

    suspend fun markChapterRead(chapterId: Int) {
        client.execute(request = MarkChapterRead(variables = MarkChapterRead.Variables(id = chapterId)))
    }

    suspend fun getAllManga(): List<Manga> {
        return client.execute(request = GetManga()).data?.mangas?.nodes?.map {
            val source = requireNotNull(it.source)
            Manga(
                suwayomiId = it.id,
                title = it.title,
                sourceName = source.name,
                sourceId = source.id,
            )
        }.orEmpty()
    }
}