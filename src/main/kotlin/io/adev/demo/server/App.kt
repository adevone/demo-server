package io.adev.demo.server

import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.*
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.serialization.json
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.postgresql.ds.PGSimpleDataSource
import java.io.File


fun main() {

    val ds = PGSimpleDataSource()
    ds.serverName = "localhost"
    ds.portNumber = 5433
    ds.databaseName = "demo"
    ds.user = "postgres"
    ds.password = "flat_alive"
    val driver = ds.asJdbcDriver()

    val database = Database(driver)
    Database.Schema.create(driver)

    val repository = Repository(database)

    embeddedServer(Netty, port = 8089) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                encodeDefaults = false
            })
        }
        routing {
            get("feed") {
                val offset = call.parameters["offset"]?.toLongOrNull()
                val limit = call.parameters["limit"]?.toLongOrNull()
                if (offset != null && offset > 10) {
                    call.respondText("No content", status = HttpStatusCode.NotFound)
                } else {
                    call.respond(MockFeedRepository.getFeed(limit, offset))
                }
            }
            get("news") {
                val offset = call.parameters["offset"]?.toLongOrNull()?.takeIf { it > 0 }
                val news = repository.getNews(offset)
                call.respond(NewsResponse(news))
            }
            post("add-source") {
                val source = call.receive<Source>()
                repository.insertSource(source)
                call.respondText("")
            }
            post("add-new") {
                val addNew = call.receive<AddNew>()
                repository.insertNew(addNew)
                call.respondText("")
            }
            post("add-like") {
                val newId = call.parameters["newId"]!!
                repository.addLike(newId)
                call.respondText("")
            }
            post("remove-like") {
                val newId = call.parameters["newId"]!!
                repository.removeLike(newId)
                call.respondText("")
            }
            post("add-dislike") {
                val newId = call.parameters["newId"]!!
                repository.addDislike(newId)
                call.respondText("")
            }
            post("remove-dislike") {
                val newId = call.parameters["newId"]!!
                repository.removeDislike(newId)
                call.respondText("")
            }
            post("subscribe") {
                val sourceId = call.parameters["newId"]!!
                repository.subscribe(sourceId)
                call.respondText("")
            }
            post("unsubscribe") {
                val sourceId = call.parameters["newId"]!!
                repository.unsubscribe(sourceId)
                call.respondText("")
            }
        }
    }.start(wait = true)
}