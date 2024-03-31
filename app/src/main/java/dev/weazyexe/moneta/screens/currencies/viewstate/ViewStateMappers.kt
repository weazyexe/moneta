package dev.weazyexe.moneta.screens.currencies.viewstate

import android.content.Context
import dev.weazyexe.moneta.R
import dev.weazyexe.moneta.core.AsyncResult
import dev.weazyexe.moneta.core.map
import dev.weazyexe.moneta.domain.model.Currency
import dev.weazyexe.moneta.screens.currencies.CurrenciesState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

fun currenciesViewState(
    state: CurrenciesState,
    context: Context
): CurrenciesViewState =
    CurrenciesViewState(
        currencies = state.currencies.toViewState(state.searchQuery, context),
        searchQuery = state.searchQuery,
        isSearchActive = state.isSearchActive
    )

private fun AsyncResult<List<Currency>>.toViewState(
    searchQuery: String,
    context: Context
): AsyncResult<ImmutableList<CurrencyViewState>> =
    map { currencies ->
        currencies
            .filter {
                it.code.contains(searchQuery, ignoreCase = true) ||
                        it.name.contains(searchQuery, ignoreCase = true) ||
                        it.symbol.contains(searchQuery, ignoreCase = true) ||
                        it.emoji.contains(searchQuery, ignoreCase = true)
            }
            .map { currency ->
                CurrencyViewState(
                    code = CurrencyViewState.Code(currency.code),
                    emoji = currency.emoji,
                    title = currency.name,
                    subtitle = if (currency.code == currency.symbol) {
                        currency.code
                    } else {
                        context.getString(
                            R.string.currencies_currency_subtitle,
                            currency.code,
                            currency.symbol
                        )
                    },
                    checked = false
                )
            }.toImmutableList()
    }