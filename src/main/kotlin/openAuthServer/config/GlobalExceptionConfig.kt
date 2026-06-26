package openAuthServer.config

import com.auth0.jwt.exceptions.JWTDecodeException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.path
import io.ktor.server.response.respond
import openAuthServer.common.getFailResponse
import org.slf4j.LoggerFactory

private val log = LoggerFactory.getLogger("GlobalExceptionHandler")

fun Application.configureStatusPages(){
    install(StatusPages){
        exception<Exception> { call, cause ->
            log.error("[Server Error] UnExpected Error - Path: {}", call.request.path(), cause)
            call.respond(getFailResponse(cause.message))
        }
        exception<JWTDecodeException>{ call, cause ->
            log.error("[Client Warning] Not Valid Token - Path: {}, Reason: {}", call.request.path(), cause.message)
            call.respond(HttpStatusCode.Unauthorized, getFailResponse("Token is not valid or has expired"))
        }
    }
}