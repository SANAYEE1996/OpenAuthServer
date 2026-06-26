package openAuthServer.di

import com.sksamuel.hoplite.ConfigLoader
import openAuthServer.config.AppConfig
import org.koin.dsl.module

val configModule = module {
    val appConfig = ConfigLoader.Companion().loadConfigOrThrow<AppConfig>("/application.conf")

    single { appConfig }

    single { get<AppConfig>().database.datasource }
    single { get<AppConfig>().oauth.google }
    single { get<AppConfig>().oauth.naver }
    single { get<AppConfig>().oauth.kakao }
    single { get<AppConfig>().jwt }
}