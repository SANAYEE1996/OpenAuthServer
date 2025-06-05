package openAuthServer.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.response.respond
import kotlinx.serialization.json.Json
import openAuthServer.config.getConfigProperty

fun Application.authConfig() {

    val repository = AuthRepository()

    install(Authentication) {
        jwt("AuthServer-JWT") {
            realm = "AuthServer-Realm"
            verifier(
                JWT
                    .require(Algorithm.HMAC256(getConfigProperty("jwt.secret")))
                    .withAudience("TICKET-API-GATEWAY")
                    .withIssuer("OPEN-AUTH-SERVER")
                    .build()
            )
            validate { credential ->
                val userInfo: UserInfo = Json.decodeFromString(credential.payload.getClaim("userInfo").asString())
                repository.findUser(userInfo.email)?.let{
                    JWTPrincipal(credential.payload)
                } ?: run{
                    null
                }
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}