package dev.weazyexe.moneta.screens.currencies

import dev.weazyexe.moneta.core.AsyncResult
import dev.weazyexe.moneta.domain.model.Currency
import dev.weazyexe.moneta.screens.currencies.viewstate.CurrencyViewState

data class CurrenciesState(
    val currencies: AsyncResult<List<Currency>> = AsyncResult.Loading(),
    val selectedCurrency: Currency? = null,
    val searchQuery: String = "",
    val isSearchActive: Boolean = false
)

sealed interface CurrenciesEvent {

    data class SelectCurrency(val selected: Currency? = null) : CurrenciesEvent

    data object OnBackClick : CurrenciesEvent

    data class OnCurrencyClick(val code: CurrencyViewState.Code) : CurrenciesEvent

    data class OnSearchFieldTextChange(val query: String) : CurrenciesEvent

    data class OnSearchActiveChange(val isActive: Boolean) : CurrenciesEvent
}

sealed interface CurrenciesEffect {

    data object GoBack : CurrenciesEffect

    data object FocusSearchTextField : CurrenciesEffect

    data class SelectCurrency(val currency: Currency) : CurrenciesEffect
}