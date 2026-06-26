package openAuthServer

import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain
import io.ktor.server.routing.routing
import openAuthServer.config.configureDatabases
import openAuthServer.config.configureHTTP
import openAuthServer.config.configureLogging
import openAuthServer.config.configureSerialization
import openAuthServer.config.configureStatusPages
import openAuthServer.di.configModule
import openAuthServer.di.repositoryModule
import openAuthServer.di.serviceModule
import openAuthServer.domain.auth.authRouting
import org.koin.ktor.plugin.koin
import org.koin.logger.slf4jLogger

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {

    koin {
        slf4jLogger()
        modules(serviceModule, configModule, repositoryModule)
    }

    configureHTTP() //cors
    configureStatusPages() //global exception handling
    configureSerialization() //serialization
    configureDatabases() //DB
    configureLogging() //logging

    routing {
        authRouting() //인증 routing
    }
}