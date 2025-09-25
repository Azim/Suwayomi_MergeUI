package ru.frozenpriest

import com.expediagroup.graphql.client.ktor.GraphQLKtorClient
import com.expediagroup.graphql.client.serialization.GraphQLClientKotlinxSerializer
import kotlinx.coroutines.runBlocking
import ru.frozenpriest.generated.NewMangaChapters
import java.net.URL

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val client = GraphQLKtorClient(
        url = URL("http://192.168.50.182:4567/api/graphql"),
        serializer = GraphQLClientKotlinxSerializer(),
    )

    runBlocking {
        val result = client.execute(NewMangaChapters(NewMangaChapters.Variables(today = "0")))
        println(result.data?.chapters?.nodes?.joinToString(separator = "\n"))
    }
}