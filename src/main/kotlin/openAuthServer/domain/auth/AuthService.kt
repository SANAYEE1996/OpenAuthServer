package openAuthServer.domain.auth

import com.auth0.jwt.exceptions.JWTDecodeException
import kotlinx.serialization.json.Json
import openAuthServer.common.OAuthProviderType

class AuthService (
    val repository: AuthRepository,
    val jwtService: JwtService,
    val googleService: OAuthGoogleService,
    val naverService: OAuthNaverService,
    val kakaoService: OAuthKakaoService,
){

    suspend fun login(code: String, type: OAuthProviderType): TokenPair{

        val oauthUserInfo: OAuthUserInfo = when(type){
            OAuthProviderType.GOOGLE -> googleService.getUserInfo(code)
            OAuthProviderType.NAVER -> naverService.getUserInfo(code)
            OAuthProviderType.KAKAO -> kakaoService.getUserInfo(code)
        }

        return jwtService.getNewJwtToken(
            UserInfo(
                "",
                oauthUserInfo.name,
                oauthUserInfo.email,
                ""
            )
        )
    }

    suspend fun getExtendAccessToken(requestRefreshToken: String): String{

        val userInfo: UserInfo = jwtService.getUserInfoFromToken(requestRefreshToken)
        if(repository.findUser(userInfo.email) != null){
            val userInfoString: String = Json.encodeToString(userInfo)
            return ""
        }
        throw JWTDecodeException("Invalid Token")
    }
}