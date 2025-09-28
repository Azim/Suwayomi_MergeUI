package ru.frozenpriest

import com.expediagroup.graphql.client.ktor.GraphQLKtorClient
import com.expediagroup.graphql.client.serialization.GraphQLClientKotlinxSerializer
import ru.frozenpriest.data.Chapter
import ru.frozenpriest.environment.Environment
import ru.frozenpriest.generated.NewMangaChapters
import java.net.URL

data object SuwayomiApi {
    private val client = GraphQLKtorClient(
        url = URL(Environment.SUWAYOMI_URL),
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
}