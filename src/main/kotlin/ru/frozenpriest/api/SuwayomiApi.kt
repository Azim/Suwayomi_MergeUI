package ru.frozenpriest.api

import com.expediagroup.graphql.client.ktor.GraphQLKtorClient
import com.expediagroup.graphql.client.serialization.GraphQLClientKotlinxSerializer
import java.net.URI
import ru.frozenpriest.generated.GetAllMangas
import ru.frozenpriest.generated.GetMangaMetadataById
import ru.frozenpriest.generated.MarkChapterRead
import ru.frozenpriest.generated.NewMangaChapters
import ru.frozenpriest.data.Chapter
import ru.frozenpriest.data.Manga
import ru.frozenpriest.data.MangaMetadata
import ru.frozenpriest.data.MangaStatus
import ru.frozenpriest.environment.Environment
import ru.frozenpriest.generated.enums.MangaStatus as SuwayomiMangaStatus

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
        return client.execute(request = GetAllMangas()).data?.mangas?.nodes?.map {
            val source = requireNotNull(it.source)
            Manga(
                suwayomiId = it.id,
                title = it.title,
                sourceName = source.name,
                sourceId = source.id,
            )
        }.orEmpty()
    }

    suspend fun getMangaMetadata(mangaId: Int): MangaMetadata {
        return client.execute(request = GetMangaMetadataById(variables = GetMangaMetadataById.Variables(id = mangaId)))
            .data?.mangas?.nodes?.first()?.let {
                MangaMetadata(
                    title = it.title,
                    description = it.description,
                    genre = it.genre,
                    status = when (it.status) {
                        SuwayomiMangaStatus.CANCELLED -> MangaStatus.ABANDONED
                        SuwayomiMangaStatus.COMPLETED -> MangaStatus.ENDED
                        SuwayomiMangaStatus.ONGOING -> MangaStatus.ONGOING
                        SuwayomiMangaStatus.ON_HIATUS -> MangaStatus.HIATUS
                        SuwayomiMangaStatus.PUBLISHING_FINISHED -> MangaStatus.ENDED
                        SuwayomiMangaStatus.LICENSED -> null
                        SuwayomiMangaStatus.UNKNOWN -> null
                        SuwayomiMangaStatus.__UNKNOWN_VALUE -> null
                    },
                )
            } ?: error("No manga with manga id $mangaId found")
    }
}
