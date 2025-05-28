package openAuthServer.config

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import openAuthServer.common.getFailResponse

fun Application.configureStatusPages(){
    install(StatusPages){
        exception<Exception> { call, cause ->
            call.respond(getFailResponse(cause.message))
        }
    }
}