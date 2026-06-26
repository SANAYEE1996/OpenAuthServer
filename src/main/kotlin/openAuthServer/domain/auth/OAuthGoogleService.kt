package openAuthServer.domain.auth

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.http.parameters
import openAuthServer.common.OAuthProviderType
import openAuthServer.config.GoogleOauthConfig

class OAuthGoogleService(
    private val config: GoogleOauthConfig,
    private val httpClient: HttpClient
):OAuthProvider {

    override val providerType = OAuthProviderType.GOOGLE

    override suspend fun getUserInfo(code: String): OAuthUserInfo {
        val accessResponse = getGoogleAccessResponse(code)
        val userInfoResponse = getGoogleUserInfo(accessResponse.accessToken)
        return OAuthUserInfo(
            providerType.name,
            userInfoResponse.sub,
            userInfoResponse.email,
            userInfoResponse.name
        )
    }

    private suspend fun getGoogleAccessResponse(code: String): GoogleOAuthTokenResponse {
        return httpClient.submitForm(
            url = config.tokenUrl,
            formParameters = parameters {
                append("code", code)
                append("client_id", config.clientId)
                append("client_secret", config.clientSecret)
                append("redirect_uri", config.redirectUrl)
                append("grant_type", config.grantType)
            }
        ).body()
    }

    private suspend fun getGoogleUserInfo(accessToken: String): GoogleOAuthUserInfoResponse {
        return httpClient.get(config.userInfoUrl) {
            bearerAuth(accessToken)
        }.body()
    }
}