package openAuthServer.auth

data class GoogleUserInfo(
    val sub: String,
    val name: String?,
    val email: String?,
    val picture: String?,
)
