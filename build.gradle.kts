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

graphql {
    client {
        schemaFile = File("schema.graphql")
        packageName = "ru.frozenpriest.generated"
        serializer = GraphQLSerializer.KOTLINX
    }
}

dependencies {
    implementation(libs.graphql.ktor)
    implementation(libs.graphql.serialization)
    implementation(libs.kermit)

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
