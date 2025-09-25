package ru.frozenpriest

import com.expediagroup.graphql.client.ktor.GraphQLKtorClient
import com.expediagroup.graphql.client.serialization.GraphQLClientKotlinxSerializer
import java.net.URL

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val client = GraphQLKtorClient(
        url = URL("http://localhost:8080/graphql"),
        serializer = GraphQLClientKotlinxSerializer(),
    )
}