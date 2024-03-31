package dev.weazyexe.moneta.di

import org.koin.dsl.module

val appModule = module {
    includes(featureModule)
}