package openAuthServer.domain.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import kotlinx.serialization.json.Json
import openAuthServer.config.JwtConfig
import java.util.Date

class JwtService (val config: JwtConfig){

    fun getNewJwtToken(param : UserInfo): TokenPair{
        val userInfoString: String = Json.encodeToString(param)
        return TokenPair(
            generateToken(userInfoString, config.accessTokenExpirationTime),
            generateToken(userInfoString, config.refreshTokenExpirationTime),
            config.accessTokenExpirationTime,
            config.refreshTokenExpirationTime
        )
    }

    fun getUserInfoFromToken(token: String): UserInfo{
        val decodedJWT = JWT.require(Algorithm.HMAC256(config.secret))
            .build()
            .verify(token)

        return Json.decodeFromString(decodedJWT.getClaim("userInfo").asString())
    }

    private fun generateToken(userInfoString: String, expiredTime: Long): String{
        return JWT.create()
            .withAudience("API-GATEWAY")
            .withIssuer("OPEN-AUTH-SERVER")
            .withClaim("userInfo", userInfoString)
            .withExpiresAt(Date(System.currentTimeMillis() + expiredTime))
            .sign(Algorithm.HMAC256(config.secret))
    }
}