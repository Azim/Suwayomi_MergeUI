import com.expediagroup.graphql.plugin.gradle.config.GraphQLSerializer
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.*

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
        endpoint = getLocalProperty("SUWAYOMI_URL")
        packageName = "ru.frozenpriest.generated"
        serializer = GraphQLSerializer.KOTLINX
    }
}

tasks.test {
    useJUnitPlatform()
}

val generateAppConstants = tasks.register("generateAppConstants") {
    val outputDir = layout.buildDirectory.dir("generated/sources/constants/kotlin")
    val packageName = "ru.frozenpriest.generated"
    val objectName = "BuildConfig"

    doLast {
        val content = """
            package $packageName
            
            object $objectName {
                const val SUWAYOMI_URL = "${getLocalProperty("SUWAYOMI_URL")}"
            }
        """.trimIndent()

        val outputFile = outputDir.get().file("${packageName.replace('.', '/')}/$objectName.kt").asFile
        outputFile.parentFile.mkdirs()
        outputFile.writeText(content)
    }

    outputs.dir(outputDir)
}

tasks.named("compileKotlin") {
    dependsOn(generateAppConstants)
}

sourceSets {
    main {
        java.srcDirs(
            layout.buildDirectory.dir("generated/sources/constants/kotlin")
        )
    }
}


fun getLocalProperty(key: String, file: String = "local.properties"): String {
    val properties = Properties()
    val localProperties = File(file)
    if (localProperties.isFile) {
        InputStreamReader(FileInputStream(localProperties), Charsets.UTF_8).use { reader ->
            properties.load(reader)
        }
    } else error("File from not found")

    return properties.getProperty(key)
}