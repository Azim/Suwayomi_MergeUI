import com.expediagroup.graphql.plugin.gradle.config.GraphQLSerializer
import io.ktor.plugin.features.DockerImageRegistry
import io.ktor.plugin.features.DockerPortMapping
import io.ktor.plugin.features.DockerPortMappingProtocol

plugins {
    kotlin("jvm") version "2.2.20"
    kotlin("plugin.serialization") version "2.2.20"
    alias(libs.plugins.graphql)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.ktor)
}

version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    compilerOptions {
        optIn = listOf(
            "kotlin.time.ExperimentalTime",
        )
    }
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

ktor {
    docker {
        localImageName.set("suwauser-docker")
        imageTag.set("0.0.1-preview")
        portMappings.set(
            listOf(
                DockerPortMapping(
                    outsideDocker = 5678,
                    insideDocker = 5678,
                    protocol = DockerPortMappingProtocol.TCP,
                )
            )
        )
        externalRegistry.set(
            DockerImageRegistry.dockerHub(
                appName = provider { "suwauser" },
                username = providers.environmentVariable("DOCKER_HUB_USERNAME"),
                password = providers.environmentVariable("DOCKER_HUB_PASSWORD")
            )
        )
    }
}

dependencies {
    implementation(libs.graphql.ktor)
    implementation(libs.graphql.serialization)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.java)
    implementation(libs.ktor.client.contentNegotiation)
    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.client.resources)
    implementation(libs.ktor.client.logging)

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.html.builder)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.status.pages)

    implementation(libs.sqldelight.driver)
    implementation(libs.kermit)
    implementation(libs.kotlin.datetime)

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
