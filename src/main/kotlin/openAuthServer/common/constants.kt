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
    FAIL("9999", "실패"),
    USER_NOT_FOUND("1001", "사용자를 찾을 수 없습니다."),
    OAUTH_GOOGLE_TOKEN_NOT_VALID("1002", "유효하지 않은 GOOGLE 토큰입니다."),
    OAUTH_GOOGLE_USER_INFO_FAILED("1003", "GOOGLE 사용자 정보 조회에 실패했습니다."),
    OAUTH_NAVER_TOKEN_NOT_VALID("1004", "유효하지 않은 NAVER 토큰입니다."),
    OAUTH_NAVER_USER_INFO_FAILED("1005", "NAVER 사용자 정보 조회에 실패했습니다."),
    OAUTH_KAKAO_TOKEN_NOT_VALID("1006", "유효하지 않은 KAKAO 토큰입니다."),
    OAUTH_KAKAO_USER_INFO_FAILED("1007", "KAKAO 사용자 정보 조회에 실패했습니다.")
    ;

    val code: String
    val message: String

    constructor(code: String, message: String) {
        this.code = code
        this.message = message
    }
}