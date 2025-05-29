package openAuthServer.auth

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class AuthLoginRequestDto(val type: String, val code: String)

@Serializable
data class GoogleTokenResponse(
    val access_token: String,
    val expires_in: Int,
    val refresh_token: String? = null,
    val scope: String,
    val token_type: String,
    val id_token: String
)

@Serializable
data class UserInfo(
    val sub: String?,
    val name: String?,
    val email: String?,
    val picture: String?,
)

@Serializable
data class GoogleUserInfo(
    val sub: String,
    val name: String?,
    val email: String?,
    val picture: String?,
)

object User : Table(){
    private val id = integer("id").autoIncrement()
    val sub = varchar("sub", 100)
    val email = varchar("email", 30)
    val name = varchar("name", 50)

    override val primaryKey = PrimaryKey(id)
}