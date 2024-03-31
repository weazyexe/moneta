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
) : MonetaScreenModel<CurrenciesState, CurrenciesViewState>(
    initialState = CurrenciesState(),
    viewStateMapper = { currenciesViewState(it, context) }
) {

    init {
        loadCurrencies()
    }

    private fun loadCurrencies() =
        async { currenciesRepository.get() }
            .onEach { setState { copy(currencies = it) } }
            .launchIn(screenModelScope)
}