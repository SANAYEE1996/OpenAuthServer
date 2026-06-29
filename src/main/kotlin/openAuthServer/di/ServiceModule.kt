package openAuthServer.di

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import openAuthServer.domain.auth.AuthService
import openAuthServer.domain.auth.JwtService
import openAuthServer.domain.auth.OAuthGoogleService
import openAuthServer.domain.auth.OAuthKakaoService
import openAuthServer.domain.auth.OAuthNaverService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.slf4j.LoggerFactory

val serviceModule = module {
    single { HttpClient(CIO){
        install(Logging) {
            logger = object : Logger {
                private val log = LoggerFactory.getLogger("HttpClient")

                override fun log(message: String) {
                    val flattened = message.lineSequence()
                        .map { it.trim() }
                        .filter { it.isNotEmpty() }
                        .joinToString(" ")
                    if (flattened.isNotEmpty()) {
                        log.info(flattened)
                    }
                }
            }
            level = LogLevel.INFO //로그 레벨 설정 : INFO로 세팅하면 URL과 200 OK/400 BadRequest 상태코드만 찍힙니다. (바디/헤더 노출 X)
        }

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    } }

    singleOf(::JwtService)
    singleOf(::OAuthGoogleService)
    singleOf(::OAuthNaverService)
    singleOf(::OAuthKakaoService)
    singleOf(::AuthService)
}