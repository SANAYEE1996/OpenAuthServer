package openAuthServer.domain.auth

import openAuthServer.common.OAuthProviderType

interface OAuthProvider {
    val providerType: OAuthProviderType
    suspend fun getUserInfo(code: String): OAuthUserInfo
}