package openAuthServer.auth

import io.ktor.http.HttpHeaders
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
            val (accessToken, refreshToken) = service.login(request.code, request.type)

            call.response.headers.append(
                HttpHeaders.SetCookie,
                "refresh_token=$refreshToken; Max-Age=3600; HttpOnly; Secure; SameSite=Lax"
            )

            call.respond(getSuccessResponse(accessToken))
        }
    }

    route("/api/v1/token-extend"){
        post{
            val request:RefreshTokenExtendRequestDto = call.receive<RefreshTokenExtendRequestDto>()
            call.respond(getSuccessResponse(service.getExtendAccessToken(request.token)))
        }
    }
}
