import com.expediagroup.graphql.plugin.gradle.config.GraphQLSerializer

plugins {
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.serialization") version "2.0.20"
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
    implementation(libs.graphql.ktor)
    implementation(libs.graphql.serialization)
    implementation(libs.sqldelight.driver)
    implementation(libs.kermit)

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
