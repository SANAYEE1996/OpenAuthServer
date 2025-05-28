package openAuthServer.auth

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.OAuthAccessTokenResponse
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.authentication
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import openAuthServer.common.getFailResponse
import openAuthServer.common.getSuccessResponse

fun Application.authRouting() {
    val httpClient = HttpClient()
    val service = AuthService()

    routing {
        authenticate("auth-oauth-google") {
            get("/login") {
                call.respondRedirect("/callback")
            }

            get("/callback") {
                val principal: OAuthAccessTokenResponse.OAuth2? = call.authentication.principal()
                if (principal != null) {
                    val accessToken = principal.accessToken

                    val userInfo: GoogleUserInfo =
                        httpClient.get("https://www.googleapis.com/oauth2/v3/userinfo") {
                            header(HttpHeaders.Authorization, "Bearer $accessToken")
                        }.body()

                    val result:GoogleUserInfo? = service.addTicket(userInfo)

                    call.respond(getSuccessResponse(result))
                } else {
                    call.respond(getFailResponse("Authenticate-Fail"))
                }
            }
        }
    }
}
