package openAuthServer.domain.auth

import io.ktor.client.HttpClient
import openAuthServer.common.OAuthProviderType
import openAuthServer.config.NaverOauthConfig

class OAuthNaverService (
    private val config: NaverOauthConfig,
    private val httpClient: HttpClient
):OAuthProvider{

    override val providerType = OAuthProviderType.NAVER

    override suspend fun getUserInfo(code: String): OAuthUserInfo {

        return OAuthUserInfo("", "", "", "")
    }
}