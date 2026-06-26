package openAuthServer.config

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.calllogging.CallLogging
import org.slf4j.event.Level
import java.util.UUID

fun Application.configureLogging() {
    install(CallLogging) {
        level = Level.INFO

        mdc("traceId") { call ->
            // 이미 앞단(API Gateway 등)에서 Trace ID를 헤더로 보내줬다면 그걸 쓰고, 없으면 새로 생성!
            call.request.headers["X-Trace-Id"] ?: UUID.randomUUID().toString()
        }
    }
}