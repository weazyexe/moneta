package dev.weazyexe.moneta.screens.convert.di

import dev.weazyexe.moneta.screens.convert.ConvertScreenModel
import org.koin.dsl.module

val convertModule = module {
    factory { ConvertScreenModel(selectedCurrenciesRepository = get()) }
}