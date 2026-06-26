package openAuthServer.config

import com.sksamuel.hoplite.ConfigAlias

data class AppConfig(
    val database: DataBaseConfig,
    val oauth: OAuthConfig,
    val jwt: JwtConfig
)

data class DataBaseConfig(
    val datasource: DataSourceConfig,
)

data class DataSourceConfig(
    @ConfigAlias("driver-class-name") val driverClassName: String,
    val url: String,
    val username: String,
    val password: String
)

data class OAuthConfig(
    val google: GoogleOauthConfig,
    val naver: NaverOauthConfig,
    val kakao: KakaoOauthConfig
)

data class GoogleOauthConfig(
    @ConfigAlias("client-id") val clientId: String,
    @ConfigAlias("client-secret") val clientSecret: String,
    @ConfigAlias("token-url") val tokenUrl: String,
    @ConfigAlias("user-info-url") val userInfoUrl: String,
    @ConfigAlias("redirect-url") val redirectUrl: String,
    @ConfigAlias("grant-type") val grantType: String,
)

data class NaverOauthConfig(
    @ConfigAlias("client-id") val clientId: String,
    @ConfigAlias("client-secret") val clientSecret: String,
    @ConfigAlias("token-url") val tokenUrl: String,
    @ConfigAlias("user-info-url") val userInfoUrl: String,
    @ConfigAlias("redirect-url") val redirectUrl: String,
    @ConfigAlias("grant-type") val grantType: String,
)

data class KakaoOauthConfig(
    @ConfigAlias("client-id") val clientId: String,
    @ConfigAlias("client-secret") val clientSecret: String,
    @ConfigAlias("token-url") val tokenUrl: String,
    @ConfigAlias("user-info-url") val userInfoUrl: String,
    @ConfigAlias("redirect-url") val redirectUrl: String,
    @ConfigAlias("grant-type") val grantType: String,
)

data class JwtConfig(
    val domain: String,
    val audience: String,
    val realm: String,
    val secret: String,
    val issuer: String,
    val accessTokenExpirationTime: Long,
    val refreshTokenExpirationTime: Long
)