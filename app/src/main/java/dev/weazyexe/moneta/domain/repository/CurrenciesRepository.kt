package dev.weazyexe.moneta.domain.repository

import dev.weazyexe.moneta.domain.model.Currency

interface CurrenciesRepository {

    suspend fun get(): List<Currency>
}