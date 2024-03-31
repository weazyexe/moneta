package dev.weazyexe.moneta.screens.currencies

import dev.weazyexe.moneta.core.AsyncResult
import dev.weazyexe.moneta.domain.model.Currency
import dev.weazyexe.moneta.screens.currencies.viewstate.CurrencyViewState

data class CurrenciesState(
    val currencies: AsyncResult<List<Currency>> = AsyncResult.Loading(),
    val searchQuery: String = "",
    val isSearchActive: Boolean = false
)

sealed interface CurrenciesEvent {

    data object OnBackClick : CurrenciesEvent

    data class OnCurrencyClick(val code: CurrencyViewState.Code) : CurrenciesEvent

    data class OnSearchFieldTextChange(val query: String) : CurrenciesEvent

    data class OnSearchActiveChange(val isActive: Boolean) : CurrenciesEvent
}

sealed interface CurrenciesEffect {

    data object FocusSearchTextField : CurrenciesEffect
}