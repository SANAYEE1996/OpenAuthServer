package openAuthServer.domain.auth

import io.ktor.client.HttpClient
import openAuthServer.config.KakaoOauthConfig

class OAuthKakaoService(
    private val config: KakaoOauthConfig,
    private val httpClient: HttpClient
) {

    suspend fun getUserInfo(code: String): OAuthUserInfo {

        return OAuthUserInfo("", "", "", "")
    }
}