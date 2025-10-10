package ru.frozenpriest

import co.touchlab.kermit.Logger
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.html.respondHtml
import io.ktor.server.http.content.staticResources
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.html.InputType
import kotlinx.html.body
import kotlinx.html.button
import kotlinx.html.head
import kotlinx.html.input
import kotlinx.html.script
import kotlinx.html.styleLink
import kotlinx.html.table
import kotlinx.html.tbody
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.thead
import kotlinx.html.title
import kotlinx.html.tr
import ru.frozenpriest.api.SuwayomiApi
import ru.frozenpriest.data.MangaWithSeries
import ru.frozenpriest.db.Database

suspend fun startServer() = embeddedServer(Netty, port = 5678) {
    install(ContentNegotiation)
    install(CallLogging)
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            Logger.e(cause) { "Received server exception" }
            call.respondText(
                text = "Internal Server Error: ${cause.message}",
                contentType = ContentType.Text.Plain,
                status = HttpStatusCode.InternalServerError
            )
        }
    }

    routing {
        apiEndpoints()
        linkTable()
        staticResources("/static", "static")
    }
}.startSuspend(wait = true)

private fun Routing.apiEndpoints() {
    post("//api/records") {
        val params = call.receiveParameters()
        val suwayomiId = params["suwayomiId"]?.toIntOrNull()
        val komgaPath = params["komgaPath"]
        val priority = params["priority"]?.toIntOrNull()

        if (suwayomiId != null && komgaPath != null && priority != null) {
            Database.insertPath(suwayomiId, komgaPath)
            call.respond(HttpStatusCode.Created)
        } else {
            call.respond(HttpStatusCode.BadRequest)
        }
    }

    delete("//api/records/{suwayomiId}") {
        val suwayomiId = call.parameters["suwayomiId"]?.toIntOrNull()
            ?: return@delete call.respond(HttpStatusCode.BadRequest)
        Database.remove(suwayomiId = suwayomiId)
        call.respond(HttpStatusCode.OK)
    }
}

private fun Routing.linkTable() {
    get("/") {
        val databaseData = Database.getAll().associateBy { it.suwayomiId }
        val data = SuwayomiApi.getAllManga().map { suwaDetails ->
            val databaseDetails = databaseData[suwaDetails.suwayomiId]
            MangaWithSeries(
                title = suwaDetails.title,
                sourceName = suwaDetails.sourceName,
                sourceId = suwaDetails.sourceId,
                suwayomiId = suwaDetails.suwayomiId,
                komgaPath = databaseDetails?.komgaPath,
                priority = databaseDetails?.priority ?: 1,
            )
        }
        call.respondHtml {
            head {
                title("Editable Records Table")
                styleLink("/static/style.css")
                script(src = "/static/script.js") {}
            }
            body {
                table("editable-table") {
                    thead {
                        tr {
                            th { +"Suwayomi ID" }
                            th { +"Source" }
                            th { +"Name" }
                            th { +"Komga Path" }
                            th { +"Priority" }
                            th { +"Actions" }
                        }
                    }
                    tbody {
                        data.forEach { record ->
                            tr("record-row") {
                                td { +"${record.suwayomiId}" }
                                td { +record.sourceName }
                                td { +record.title }
                                td {
                                    input(type = InputType.text, classes = "komga-path") {
                                        record.komgaPath?.let { value = it }
                                    }
                                }
                                td {
                                    input(type = InputType.number, classes = "priority") {
                                        value = record.priority.toString()
                                    }
                                }
                                td {
                                    button(classes = "save-btn") { +"Save" }
                                    button(classes = "reset-btn") { +"Reset" }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
