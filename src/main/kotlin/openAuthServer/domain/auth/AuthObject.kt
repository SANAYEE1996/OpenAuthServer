package openAuthServer.domain.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class AuthLoginRequestDto(val code: String)

@Serializable
data class RefreshTokenExtendRequestDto(val token: String)

@Serializable
data class UserInfo(
    val sub: String?,
    val name: String,
    val email: String,
    val picture: String?,
)

@Serializable
data class OAuthUserInfo(
    val provider: String,
    val providerId: String,
    val email: String,
    val name: String,
)

object User : Table(){
    private val id = integer("id").autoIncrement()
    val sub = varchar("sub", 100)
    val email = varchar("email", 30)
    val name = varchar("name", 50)

    override val primaryKey = PrimaryKey(id)
}

data class TokenPair(val accessToken: String, val refreshToken: String, val accessExpiresIn: Long, val refreshExpiresIn: Long)

@Serializable
data class GoogleOAuthTokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("expires_in") val expiresIn: Long,
    @SerialName("scope") val scope: String,
    @SerialName("token_type") val tokenType: String,
    @SerialName("id_token") val idToken: String? = null,
    @SerialName("refresh_token") val refreshToken: String? = null
)

@Serializable
data class GoogleOAuthUserInfoResponse(
    @SerialName("sub") val sub: String,
    @SerialName("email") val email: String,
    @SerialName("email_verified") val emailVerified: Boolean,
    @SerialName("name") val name: String,
    @SerialName("picture") val picture: String? = null,
    @SerialName("given_name") val givenName: String? = null,
    @SerialName("family_name") val familyName: String? = null,
    @SerialName("locale") val locale: String? = null,
)

@Serializable
data class NaverOAuthTokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String? = null,
    @SerialName("token_type") val tokenType: String,
    @SerialName("expires_in") val expiresIn: Long,
)

@Serializable
data class NaverOAuthUserInfoResponse(
    @SerialName("resultcode") val resultCode: String,
    @SerialName("message") val message: String,
    @SerialName("response") val userInfo: NaverOAuthUserInfo,
)

@Serializable
data class NaverOAuthUserInfo(
    @SerialName("id") val id: String,
    @SerialName("email") val email: String,
    @SerialName("name") val name: String,
    @SerialName("nickname") val nickname: String? = null,
    @SerialName("gender") val gender: String? = null,
    @SerialName("age") val age: String? = null,
    @SerialName("birthday") val birthday: String? = null,
    @SerialName("profile_image") val profileImage: String? = null,
    @SerialName("birthyear") val birthYear: String? = null,
    @SerialName("mobile") val mobile: String? = null,
)

@Serializable
data class KakaoOAuthTokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String,
    @SerialName("token_type") val tokenType: String,
    @SerialName("expires_in") val expiresIn: Int? = null,
    @SerialName("scope") val scope: String? = null,
    @SerialName("refresh_token_expires_in") val refreshTokenExpiresIn: Int? = null,
)

@Serializable
data class KakaoOAuthUserInfoResponse(
    @SerialName("id") val id: Long,
    @SerialName("connected_at") val connectedAt: String,
    @SerialName("properties") val properties: KakaoOAuthUserProperties,
    @SerialName("kakao_account") val kakaoAccount: KakaoOAuthUserAccount,
)

@Serializable
data class KakaoOAuthUserProperties(
    @SerialName("nickname") val nickname: String? = null,
)

@Serializable
data class KakaoOAuthUserAccount(
    @SerialName("profile_nickname_needs_agreement") val nickNameAgreement: Boolean? = null,
    @SerialName("has_email") val hasEmail: Boolean? = null,
    @SerialName("email_needs_agreement") val emailNeedsAgreement: Boolean? = null,
    @SerialName("is_email_valid") val isEmailValid: Boolean? = null,
    @SerialName("is_email_verified") val isEmailVerified: Boolean? = null,
    @SerialName("email") val email: String,
    @SerialName("profile") val profile: KakaoOAuthUserProfile,
)

@Serializable
data class KakaoOAuthUserProfile(
    @SerialName("nickname") val nickname: String,
    @SerialName("is_default_nickname") val isDefaultNickName: Boolean? = null,
)