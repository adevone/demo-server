package io.adev.demo.server

import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.response.respond
import io.ktor.routing.get
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

    val newsRepository = NewsRepository(database)
    try {
        val initNewsText = File("init_news.json").readText()
        val initNews = Json.decodeFromString(ListSerializer(New.serializer()), initNewsText)
        initNews.forEach { new ->
            newsRepository.insert(new)
        }
    } catch (e: Exception) {
        println("Can not insert default news")
    }

    embeddedServer(Netty, port = 8089) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                encodeDefaults = false
            })
        }
        routing {
            get("news") {
                val news = newsRepository.all()
                call.respond(news)
            }
        }
    }.start(wait = true)
}