package dev.weazyexe.moneta.screens.currencies

import android.content.Context
import cafe.adriel.voyager.core.model.screenModelScope
import dev.weazyexe.moneta.core.MonetaScreenModel
import dev.weazyexe.moneta.domain.repository.CurrenciesRepository
import dev.weazyexe.moneta.screens.currencies.viewstate.CurrenciesViewState
import dev.weazyexe.moneta.screens.currencies.viewstate.currenciesViewState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class CurrenciesScreenModel(
    private val currenciesRepository: CurrenciesRepository,
    private val context: Context
) : MonetaScreenModel<CurrenciesState, CurrenciesViewState, CurrenciesEvent, CurrenciesEffect>(
    initialState = CurrenciesState(),
    viewStateMapper = { currenciesViewState(it, context) }
) {

    init {
        loadCurrencies()
    }

    override fun sink(event: CurrenciesEvent) {
        when (event) {
            is CurrenciesEvent.OnBackClick -> handleBackClick()
            is CurrenciesEvent.OnCurrencyClick -> handleCurrencyClick(event)
            is CurrenciesEvent.OnSearchActiveChange -> changeSearchActivation(event)
            is CurrenciesEvent.OnSearchFieldTextChange -> changeSearchFieldText(event)
        }
    }

    private fun loadCurrencies() =
        async { currenciesRepository.get() }
            .onEach { setState { copy(currencies = it) } }
            .launchIn(screenModelScope)

    private fun handleBackClick() {
        if (state.value.isSearchActive) {
            sink(CurrenciesEvent.OnSearchActiveChange(isActive = false))
        } else {
            // Do navigation stuff
        }
    }

    private fun handleCurrencyClick(event: CurrenciesEvent.OnCurrencyClick) {
        // Handle somehow
    }

    private fun changeSearchActivation(event: CurrenciesEvent.OnSearchActiveChange) {
        setState {
            copy(
                isSearchActive = event.isActive,
                searchQuery = ""
            )
        }

        if (event.isActive) {
            send(CurrenciesEffect.FocusSearchTextField)
        }
    }

    private fun changeSearchFieldText(event: CurrenciesEvent.OnSearchFieldTextChange) {
        setState { copy(searchQuery = event.query) }
    }
}