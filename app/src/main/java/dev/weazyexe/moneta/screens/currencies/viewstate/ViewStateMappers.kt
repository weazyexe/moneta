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
        currencies = state.currencies.toViewState(context)
    )

private fun AsyncResult<List<Currency>>.toViewState(context: Context): AsyncResult<ImmutableList<CurrencyViewState>> =
    map {
        it.map { currency ->
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