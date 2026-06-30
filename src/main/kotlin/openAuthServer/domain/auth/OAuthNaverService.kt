package openAuthServer.domain.auth

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import openAuthServer.common.CommonCode
import openAuthServer.common.CustomException
import openAuthServer.common.OAuthProviderType
import openAuthServer.config.NaverOauthConfig
import org.slf4j.LoggerFactory

private val log = LoggerFactory.getLogger("OAuthNaverService")

class OAuthNaverService (
    private val config: NaverOauthConfig,
    private val httpClient: HttpClient
){

    suspend fun getUserInfo(code: String): OAuthUserInfo {
        val accessResponse = getNaverAccessResponse(code)
        val userInfoResponse = getNaverUserInfo(accessResponse.accessToken)
        log.info("Naver 유저 정보 수신 완료 - Email: {}, Name: {}", userInfoResponse.email, userInfoResponse.name)
        return OAuthUserInfo(
            OAuthProviderType.NAVER.name,
            userInfoResponse.id,
            userInfoResponse.email,
            userInfoResponse.name
        )
    }

    private suspend fun getNaverAccessResponse(code: String): NaverOAuthTokenResponse = try {
        httpClient.get(config.tokenUrl) {
            parameter("grant_type", config.grantType)
            parameter("client_id", config.clientId)
            parameter("client_secret", config.clientSecret)
            parameter("code", code)
            header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
        }.body()
    } catch(e: Exception) {
        log.error("Naver Access Token 요청 실패: ${e.message}")
        throw CustomException(CommonCode.OAUTH_NAVER_TOKEN_NOT_VALID)
    }

    private suspend fun getNaverUserInfo(accessToken: String): NaverOAuthUserInfo {

        val response: NaverOAuthUserInfoResponse = try {
            httpClient.get(config.userInfoUrl) {
                bearerAuth(accessToken)
            }.body()
        } catch (e: Exception) {
            log.error("Naver User Info 네트워크 요청 실패", e)
            throw CustomException(CommonCode.OAUTH_NAVER_USER_INFO_FAILED)
        }

        if(response.resultCode != "00") {
            log.error("Naver User Info 응답 에러 -> 코드: ${response.resultCode}, 메세지: ${response.message}")
            throw CustomException(CommonCode.OAUTH_NAVER_USER_INFO_FAILED)
        }

        return response.userInfo
    }
}