package dev.weazyexe.moneta.di

import dev.weazyexe.moneta.screens.currencies.di.currenciesModule
import org.koin.dsl.module

private val modules = listOf(
    currenciesModule
)

val featureModule = module {
    includes(dataModule + modules)
}