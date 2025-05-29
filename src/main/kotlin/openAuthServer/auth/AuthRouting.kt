package openAuthServer.auth

import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.route
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import openAuthServer.common.getSuccessResponse

fun Route.authRouting() {
    val service = AuthService()

    route("/api/v1/login"){
        post{
            val request:AuthLoginRequestDto = call.receive<AuthLoginRequestDto>()
            call.respond(getSuccessResponse(service.getUserInfo(request.code, request.type)))
        }
    }
}
