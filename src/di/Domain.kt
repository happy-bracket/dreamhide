package net.rocketparty.di

import net.rocketparty.interactor.AuthInteractor
import org.koin.dsl.module.module

val DomainModule = module {
    single { AuthInteractor(get()) }
}