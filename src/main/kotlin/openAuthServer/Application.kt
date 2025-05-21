package openAuthServer

import io.ktor.server.application.*
import openAuthServer.config.configureHTTP
import openAuthServer.config.configureRouting
import openAuthServer.config.configureSecurity

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSecurity()
    configureHTTP()
    configureRouting()
}
