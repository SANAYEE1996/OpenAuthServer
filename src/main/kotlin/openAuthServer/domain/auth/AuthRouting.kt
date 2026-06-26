package openAuthServer.domain.auth

import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.route
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import openAuthServer.common.OAuthProviderType
import openAuthServer.common.getSuccessResponse

fun Route.authRouting(authService: AuthService) {

    route("/api/v1/auth"){
        post("/{provider}/login"){
            val providerType = call.getProviderOrThrow()
            val request: AuthLoginRequestDto = call.receive<AuthLoginRequestDto>()
            val tokens: TokenPair = authService.login(request.code, providerType)
            call.setAuthCookies(tokens)
            call.respond(getSuccessResponse("로그인 성공"))
        }
        post("/extend"){
            val request:RefreshTokenExtendRequestDto = call.receive<RefreshTokenExtendRequestDto>()
            call.respond(getSuccessResponse(authService.getExtendAccessToken(request.token)))
        }
    }
}

fun ApplicationCall.setAuthCookies(tokens: TokenPair) {
    this.response.cookies.append(
        name = "access_token",
        value = tokens.accessToken,
        maxAge = tokens.accessExpiresIn,
        httpOnly = true,
        secure = true,
        extensions = mapOf("SameSite" to "Lax")
    )
    this.response.cookies.append(
        name = "refresh_token",
        value = tokens.refreshToken,
        maxAge = tokens.refreshExpiresIn,
        httpOnly = true,
        secure = true,
        extensions = mapOf("SameSite" to "Lax")
    )
}

fun ApplicationCall.getProviderOrThrow(): OAuthProviderType {
    val providerStr = this.parameters["provider"]
        ?: throw IllegalArgumentException("Provider 파라미터가 누락되었습니다.")

    return OAuthProviderType.fromString(providerStr)
}