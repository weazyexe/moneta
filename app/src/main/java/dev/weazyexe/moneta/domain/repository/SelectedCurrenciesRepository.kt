package dev.weazyexe.moneta.domain.repository

import dev.weazyexe.moneta.domain.model.Currency

interface SelectedCurrenciesRepository {

    val currenciesRepository: CurrenciesRepository

    suspend fun getFrom(): Currency

    suspend fun getTo(): Currency

    suspend fun putFrom(currency: Currency)

    suspend fun putTo(currency: Currency)

    suspend fun getDefaultFrom(): Currency =
        currenciesRepository.get().first { it.code == DEFAULT_FROM_CURRENCY_CODE }

    suspend fun getDefaultTo(): Currency =
        currenciesRepository.get().first { it.code == DEFAULT_TO_CURRENCY_CODE }

    companion object {

        const val DEFAULT_FROM_CURRENCY_CODE = "USD"
        const val DEFAULT_TO_CURRENCY_CODE = "RUB"
    }
}