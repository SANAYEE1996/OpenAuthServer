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