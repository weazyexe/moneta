package dev.weazyexe.moneta.screens.currencies.di

import dev.weazyexe.moneta.screens.currencies.CurrenciesScreenModel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val currenciesModule = module {
    factory {
        CurrenciesScreenModel(
            currenciesRepository = get(),
            context = androidContext()
        )
    }
}