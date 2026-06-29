package openAuthServer.domain.auth

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.http.parameters
import openAuthServer.common.OAuthProviderType
import openAuthServer.config.GoogleOauthConfig
import org.slf4j.LoggerFactory

private val log = LoggerFactory.getLogger("OAuthGoogleService")

class OAuthGoogleService(
    private val config: GoogleOauthConfig,
    private val httpClient: HttpClient
) {

    suspend fun getUserInfo(code: String): OAuthUserInfo {
        val accessResponse = getGoogleAccessResponse(code)
        val userInfoResponse = getGoogleUserInfo(accessResponse.accessToken)
        log.info("Google 유저 정보 수신 완료 - Email: {}, Name: {}", userInfoResponse.email, userInfoResponse.name)
        return OAuthUserInfo(
            OAuthProviderType.GOOGLE.name,
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