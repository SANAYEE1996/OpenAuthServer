package openAuthServer.config

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.json.Json

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json {
            isLenient = true
            ignoreUnknownKeys = true // 외부 API(카카오 등) 스펙 변경 대비 필수 설정
        })
    }
}