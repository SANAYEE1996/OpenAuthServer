package openAuthServer

import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondText
import io.ktor.server.routing.routing
import io.ktor.server.routing.get
import openAuthServer.auth.authConfig
import openAuthServer.auth.authRouting
import openAuthServer.config.logging
import openAuthServer.config.DatabaseConfig
import openAuthServer.config.configureHTTP
import openAuthServer.config.configureStatusPages

fun main() {
    embeddedServer(Netty, port = 8080, module = Application::module).start(wait = true)
}

fun Application.module() {
    DatabaseConfig.init()
    configureHTTP()
    configureStatusPages()
    logging()
    authConfig()
    configureRouting()
}

fun Application.configureRouting() {
    routing{
        authenticate("AuthServer-JWT"){
            get("/api/v1/user-list") {
                call.respondText("Ticket Page")
            }
        }
        authRouting()
    }
}
