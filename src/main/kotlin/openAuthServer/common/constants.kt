package openAuthServer.common

enum class OAuthProviderType {
    GOOGLE, KAKAO, NAVER;

    companion object {
        fun fromString(type: String): OAuthProviderType {
            return runCatching { valueOf(type.uppercase()) }
                .getOrElse { throw IllegalArgumentException("지원하지 않는 소셜 로그인 타입입니다: $type") }
        }
    }
}

enum class CommonCode {
    SUCCESS("0000", "성공"),
    FAIL("9999", "실패");

    val code: String
    val message: String

    constructor(code: String, message: String) {
        this.code = code
        this.message = message
    }
}