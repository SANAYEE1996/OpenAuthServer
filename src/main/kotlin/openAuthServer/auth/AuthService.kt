package openAuthServer.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.get
import io.ktor.client.request.setBody
import io.ktor.client.request.header
import io.ktor.http.contentType
import io.ktor.http.ContentType
import io.ktor.http.formUrlEncode
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import openAuthServer.config.getConfigProperty
import java.util.Date

class AuthService {

    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    private val repository: AuthRepository = AuthRepository()

    suspend fun login(code: String, type: String): Pair<String, String>{
        if(type == "GOOGLE"){
            val tokenResponse: GoogleTokenResponse = httpClient.post("https://oauth2.googleapis.com/token") {
                contentType(ContentType.Application.FormUrlEncoded)
                setBody(
                    listOf(
                        "code" to code,
                        "client_id" to getConfigProperty("oauth.client-id"),
                        "client_secret" to getConfigProperty("oauth.client-secret"),
                        "redirect_uri" to "http://localhost:4000/google-oauth2/callback",
                        "grant_type" to "authorization_code",
                    ).formUrlEncode()
                )
            }.body()

            val googleUserInfo: GoogleUserInfo = httpClient.get("https://www.googleapis.com/oauth2/v3/userinfo") {
                header(HttpHeaders.Authorization, "Bearer ${tokenResponse.access_token}")
            }.body()

            return getJwtToken(UserInfo(googleUserInfo.sub, googleUserInfo.name ?: run{""}, googleUserInfo.email, googleUserInfo.picture))
        }
        throw RuntimeException("Type Must Be 'Google'")
    }

    suspend fun getExtendAccessToken(requestRefreshToken: String): String{
        val decodedJWT = JWT.require(Algorithm.HMAC256(getConfigProperty("jwt.secret")))
            .build()
            .verify(requestRefreshToken)

        val userInfo: UserInfo = Json.decodeFromString(decodedJWT.getClaim("userInfo").asString())
        if(repository.findUser(userInfo.email) != null){
            val userInfoString: String = Json.encodeToString(userInfo)
            return getJwtToken(userInfoString, 30 * 60 * 1000)
        }
        throw JWTDecodeException("Invalid Token")
    }

    private suspend fun getJwtToken(param : UserInfo): Pair<String, String>{
        val user = addUser(param)
        val userInfoString: String = Json.encodeToString(user)
        return Pair(getJwtToken(userInfoString, 30 * 60 * 1000), getJwtToken(userInfoString, 60 * 60 * 1000))
    }

    private fun getJwtToken(userInfoString: String, expiredTime: Int): String{
        return JWT.create()
            .withAudience("TICKET-API-GATEWAY")
            .withIssuer("OPEN-AUTH-SERVER")
            .withClaim("userInfo", userInfoString)
            .withExpiresAt(Date(System.currentTimeMillis() + expiredTime))
            .sign(Algorithm.HMAC256(getConfigProperty("jwt.secret")))
    }

    private suspend fun addUser(data: UserInfo) : UserInfo? {
        if(repository.findUser(data.email) != null){
            return data
        }
        return repository.insertUser(data)
    }
}