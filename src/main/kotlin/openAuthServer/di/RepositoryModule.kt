package openAuthServer.di

import openAuthServer.domain.auth.AuthRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::AuthRepository)
}