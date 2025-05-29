package openAuthServer

import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respondText
import io.ktor.server.routing.routing
import io.ktor.server.routing.get
import openAuthServer.auth.authRouting
import openAuthServer.config.logging
import openAuthServer.config.DatabaseConfig
import openAuthServer.config.configureHTTP
import openAuthServer.config.configureStatusPages

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    DatabaseConfig.init()
    configureHTTP()
    configureStatusPages()
    logging()
    configureRouting()
}

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Ticket Page")
        }
    }
    routing{
        authRouting()
    }
}
