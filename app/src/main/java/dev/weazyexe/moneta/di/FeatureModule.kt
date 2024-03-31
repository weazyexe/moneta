package dev.weazyexe.moneta.di

import dev.weazyexe.moneta.screens.convert.di.convertModule
import dev.weazyexe.moneta.screens.currencies.di.currenciesModule
import org.koin.dsl.module

private val modules = listOf(
    currenciesModule,
    convertModule
)

val featureModule = module {
    includes(dataModule + modules)
}