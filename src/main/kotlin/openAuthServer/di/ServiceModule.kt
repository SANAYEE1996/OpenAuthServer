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
import openAuthServer.domain.auth.OAuthProvider
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.slf4j.LoggerFactory

val serviceModule = module {
    single { HttpClient(CIO){
        install(Logging) {
            logger = object : Logger {
                private val delegate = LoggerFactory.getLogger("HttpClient") // slf4j logger를 Ktor HttpClient에 연결
                override fun log(message: String) {
                    delegate.info(message)
                }
            }
            level = LogLevel.BODY //로그 레벨 설정 (BODY로 하면 요청/응답 파라미터와 JSON 본문까지 다 로그에 찍힘)
        }

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    } }

    single {
        AuthService(
            repository = get(),
            jwtService= get(),
            providers = getAll()
        )
    }
    singleOf(::JwtService)
    singleOf(::OAuthGoogleService) bind OAuthProvider::class
    singleOf(::OAuthNaverService) bind OAuthProvider::class
    singleOf(::OAuthKakaoService) bind OAuthProvider::class
}