package openAuthServer.auth

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

class AuthService {

    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    private val repository: AuthRepository = AuthRepository()

    suspend fun getUserInfo(code: String, type: String): UserInfo?{
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

            val userInfo = UserInfo(googleUserInfo.sub, googleUserInfo.name, googleUserInfo.email, googleUserInfo.picture)

            return addUser(userInfo)
        }
        return UserInfo("", "", "", "")
    }

    private suspend fun addUser(data: UserInfo) : UserInfo? {
        if(repository.findUser(data.email.orEmpty()) != null){
            return data
        }
        return repository.insertUser(data)
    }
}