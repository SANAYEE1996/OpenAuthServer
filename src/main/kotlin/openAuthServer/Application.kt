package openAuthServer

import io.ktor.server.application.Application
import openAuthServer.auth.authRouting
import openAuthServer.auth.configureSecurity
import openAuthServer.config.DatabaseConfig
import openAuthServer.config.configureHTTP

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    DatabaseConfig.init()
    configureSecurity()
    configureHTTP()
    authRouting()
}
