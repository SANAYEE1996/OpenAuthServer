package openAuthServer.domain.auth

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.parameters
import openAuthServer.common.CommonCode
import openAuthServer.common.CustomException
import openAuthServer.common.OAuthProviderType
import openAuthServer.config.KakaoOauthConfig
import org.slf4j.LoggerFactory

private val log = LoggerFactory.getLogger("OAuthKakaoService")

class OAuthKakaoService(
    private val config: KakaoOauthConfig,
    private val httpClient: HttpClient
) {

    suspend fun getUserInfo(code: String): OAuthUserInfo {
        val accessResponse = getKakaoAccessToken(code)
        val userInfoResponse = getKakaoUserInfo(accessResponse.accessToken)
        log.info("Kakao 유저 정보 수신 완료 - Email: {}, Name: {}", userInfoResponse.kakaoAccount.email, userInfoResponse.kakaoAccount.profile.nickname)
        return OAuthUserInfo(
            OAuthProviderType.KAKAO.name,
            userInfoResponse.id.toString(),
            userInfoResponse.kakaoAccount.email,
            userInfoResponse.kakaoAccount.profile.nickname
        )
    }

    private suspend fun getKakaoAccessToken(code: String): KakaoOAuthTokenResponse = try {
        httpClient.submitForm(
            url = config.tokenUrl,
            formParameters = parameters {
                append("code", code)
                append("client_id", config.clientId)
                append("client_secret", config.clientSecret)
                append("redirect_uri", config.redirectUrl)
                append("grant_type", config.grantType)
            }
        ).body()
    } catch (e: Exception) {
        log.error("Naver Access Token 요청 실패: ${e.message}")
        throw CustomException(CommonCode.OAUTH_KAKAO_TOKEN_NOT_VALID)
    }

    private suspend fun getKakaoUserInfo(accessToken: String): KakaoOAuthUserInfoResponse = try {
        httpClient.get(config.userInfoUrl) {
            bearerAuth(accessToken)
            header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
        }.body()
    } catch (e: Exception) {
        log.error("Kakao User Info 요청 실패: ${e.message}")
        throw CustomException(CommonCode.OAUTH_KAKAO_USER_INFO_FAILED)
    }
}