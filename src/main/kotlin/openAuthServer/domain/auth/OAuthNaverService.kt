package openAuthServer.domain.auth

import io.ktor.client.HttpClient
import openAuthServer.config.NaverOauthConfig

class OAuthNaverService (
    private val config: NaverOauthConfig,
    private val httpClient: HttpClient
){

    suspend fun getUserInfo(code: String): OAuthUserInfo {

        return OAuthUserInfo("", "", "", "")
    }
}