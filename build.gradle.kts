import com.expediagroup.graphql.plugin.gradle.config.GraphQLSerializer

plugins {
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.serialization") version "2.0.20"
    alias(libs.plugins.graphql)
}

group = "ru.frozenpriest"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.graphql.ktor)
    testImplementation(kotlin("test"))
}

graphql {
    client {
        endpoint = "http://192.168.50.182:4567/api/graphql"
        packageName = "ru.frozenpriest.generated"
        serializer = GraphQLSerializer.KOTLINX
    }
}

tasks.test {
    useJUnitPlatform()
}