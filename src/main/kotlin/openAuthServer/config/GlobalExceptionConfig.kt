package openAuthServer.config

import com.auth0.jwt.exceptions.JWTDecodeException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.ParameterConversionException
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
            call.respond(HttpStatusCode.InternalServerError, getFailResponse(cause.message?:"Unknown Error"))
        }
        exception<JWTDecodeException>{ call, cause ->
            log.error("[Client Warning] Not Valid Token - Path: {}, Reason: {}", call.request.path(), cause.message)
            call.respond(HttpStatusCode.Unauthorized, getFailResponse(cause.message?:"Token is not valid or has expired"))
        }
        exception<BadRequestException> { call, cause ->
            log.error("[Client Error] Invalid Request Body - Path: {}, Reason: {}", call.request.path(), cause.message)
            call.respond(HttpStatusCode.BadRequest, getFailResponse("요청 바디의 형식이 올바르지 않습니다."))
        }
        exception<ParameterConversionException> { call, cause ->
            log.error("[Client Error] Invalid Parameter - Path: {}, Parameter: {}", call.request.path(), cause.parameterName)
            call.respond(HttpStatusCode.BadRequest, getFailResponse("파라미터 값이 유효하지 않습니다."))
        }
        exception<IllegalArgumentException> { call, cause ->
            log.error("[Business Warning] Validation Failed - Path: {}, Msg: {}", call.request.path(), cause.message)
            call.respond(HttpStatusCode.BadRequest, getFailResponse(cause.message ?: "잘못된 요청입니다."))
        }
        exception<IllegalStateException> { call, cause ->
            log.error("[Business Warning] State Invalid - Path: {}, Msg: {}", call.request.path(), cause.message)
            call.respond(HttpStatusCode.Conflict, getFailResponse(cause.message ?: "요청을 처리할 수 없는 상태입니다."))
        }
    }
}