package dev.weazyexe.moneta.di

import dev.weazyexe.moneta.data.CurrenciesRepositoryImpl
import dev.weazyexe.moneta.data.SelectedCurrenciesRepositoryImpl
import dev.weazyexe.moneta.domain.repository.CurrenciesRepository
import dev.weazyexe.moneta.domain.repository.SelectedCurrenciesRepository
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single<Json> {
        Json { ignoreUnknownKeys = true }
    }

    single<CurrenciesRepository> {
        CurrenciesRepositoryImpl(
            context = androidContext(),
            json = get()
        )
    }

    single<SelectedCurrenciesRepository> {
        SelectedCurrenciesRepositoryImpl(
            currenciesRepository = get(),
            context = androidContext()
        )
    }
}