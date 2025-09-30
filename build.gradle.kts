import com.expediagroup.graphql.plugin.gradle.config.GraphQLSerializer

plugins {
    kotlin("jvm") version "2.2.20"
    kotlin("plugin.serialization") version "2.2.20"
    alias(libs.plugins.graphql)
    alias(libs.plugins.sqldelight)
}

group = "ru.frozenpriest"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

graphql {
    client {
        schemaFile = File("schema.graphql")
        packageName = "ru.frozenpriest.generated"
        serializer = GraphQLSerializer.KOTLINX
    }
}

sqldelight {
    databases {
        create("Database") {
            packageName.set("ru.frozenpriest")
        }
    }
}

dependencies {
    implementation(libs.graphql.spring)
    implementation(libs.graphql.serialization)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.java)
    implementation(libs.ktor.client.contentNegotiation)
    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.client.resources)

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.html.builder)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.status.pages)

    implementation(libs.sqldelight.driver)
    implementation(libs.kermit)

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
