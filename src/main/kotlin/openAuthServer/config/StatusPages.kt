package openAuthServer.config

import com.auth0.jwt.exceptions.JWTDecodeException
import io.ktor.http.HttpStatusCode
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
        exception<JWTDecodeException>{ call, cause ->
            call.respond(HttpStatusCode.Unauthorized, getFailResponse("Token is not valid or has expired"))
        }
    }
}