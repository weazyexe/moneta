package dev.weazyexe.moneta.screens.currencies.viewstate

import androidx.compose.runtime.Immutable
import dev.weazyexe.moneta.core.AsyncResult
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class CurrenciesViewState(
    val currencies: AsyncResult<ImmutableList<CurrencyViewState>>,
    val searchQuery: String,
    val isSearchActive: Boolean
)

@Immutable
data class CurrencyViewState(
    val code: Code,
    val emoji: String,
    val title: String,
    val subtitle: String,
    val checked: Boolean
) {

    @JvmInline
    value class Code(val origin: String)
}