package openAuthServer.config

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.cors.routing.CORS

fun Application.configureHTTP() {
    install(CORS) {
        // 🌟 1. Next.js가 구동 중인 주소와 포트를 정확하게 허용해줍니다.
        // 유저님 conf 파일에 4000번 포트가 보였으니 scheme을 포함해 명시합니다.
        allowHost("localhost:4000", schemes = listOf("http"))

        // 🌟 2. 소셜 로그인 시 사용할 HTTP 메서드들을 허용합니다.
        allowMethod(HttpMethod.Options) // 브라우저가 본 요청 전 날리는 예비 요청(Preflight) 필수!
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Put)

        // 🌟 3. 프론트엔드와 주고받을 헤더들을 허용합니다.
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)

        // 🌟 4. [초특급 중요] 쿠키를 주고받으려면 이 옵션이 무조건 true여야 합니다!
        // 이 옵션이 켜지면 allowHost()에 와일드카드(*)를 쓸 수 없고 위처럼 주소를 명시해야 합니다.
        allowCredentials = true
    }
}
