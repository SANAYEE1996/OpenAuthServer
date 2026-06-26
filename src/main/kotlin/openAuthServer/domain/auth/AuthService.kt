package openAuthServer.domain.auth

import com.auth0.jwt.exceptions.JWTDecodeException
import kotlinx.serialization.json.Json
import openAuthServer.common.OAuthProviderType

class AuthService (
    val repository: AuthRepository,
    val jwtService: JwtService,
    providers: List<OAuthProvider>
){

    private val providerRegistry: Map<OAuthProviderType, OAuthProvider> =
        providers.associateBy { it.providerType }

    suspend fun login(code: String, type: OAuthProviderType): TokenPair{
        val providerService = providerRegistry[type]
            ?: throw IllegalStateException("${type.name} 서비스 구현체가 존재하지 않습니다.")
        val oauthUserInfo: OAuthUserInfo = providerService.getUserInfo(code)
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