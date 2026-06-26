package openAuthServer.domain.auth

import io.ktor.client.HttpClient
import openAuthServer.common.OAuthProviderType
import openAuthServer.config.KakaoOauthConfig

class OAuthKakaoService(
    private val config: KakaoOauthConfig,
    private val httpClient: HttpClient
):OAuthProvider {

    override val providerType = OAuthProviderType.KAKAO

    override suspend fun getUserInfo(code: String): OAuthUserInfo {

        return OAuthUserInfo("", "", "", "")
    }
}