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

fun Application.authConfig() {
    install(Authentication) {
        jwt("auth-jwt") {
            realm = "ktor-sample"
            verifier(
                JWT
                    .require(Algorithm.HMAC256("secret"))
                    .withAudience("ktor-audience")
                    .withIssuer("ktor-issuer")
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim("name").asString().isNotEmpty()) {
                    JWTPrincipal(credential.payload)
                } else null
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}