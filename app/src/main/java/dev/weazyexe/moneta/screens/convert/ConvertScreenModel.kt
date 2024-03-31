package dev.weazyexe.moneta.screens.convert

import cafe.adriel.voyager.core.model.screenModelScope
import dev.weazyexe.moneta.core.AsyncResult
import dev.weazyexe.moneta.core.MonetaScreenModel
import dev.weazyexe.moneta.domain.model.Currency
import dev.weazyexe.moneta.domain.repository.SelectedCurrenciesRepository
import dev.weazyexe.moneta.screens.convert.viewstate.ConvertViewState
import dev.weazyexe.moneta.screens.convert.viewstate.convertViewState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ConvertScreenModel(
    private val selectedCurrenciesRepository: SelectedCurrenciesRepository
) : MonetaScreenModel<ConvertState, ConvertViewState, ConvertEvent, ConvertEffect>(
    initialState = ConvertState.Loading,
    viewStateMapper = ::convertViewState
) {

    private val stateContent: ConvertState.Content?
        get() = state.value as? ConvertState.Content

    init {
        loadSelectedCurrencies()
    }

    override fun sink(event: ConvertEvent) {
        when (event) {
            is ConvertEvent.FromAmountChange -> changeFromAmount(event)
            is ConvertEvent.ToAmountChange -> changeToAmount(event)

            is ConvertEvent.SelectFromCurrency -> changeFromCurrency(event)
            is ConvertEvent.SelectToCurrency -> changeToCurrency(event)

            is ConvertEvent.OnFromCurrencyClick -> handleFromCurrencyClick(event)
            is ConvertEvent.OnToCurrencyClick -> handleToCurrencyClick(event)
        }
    }

    private fun loadSelectedCurrencies() {
        async {
            val from = selectedCurrenciesRepository.getFrom()
            val to = selectedCurrenciesRepository.getTo()

            from to to
        }
            .onEach {
                when (it) {
                    is AsyncResult.Error -> setState { ConvertState.Error }
                    is AsyncResult.Loading -> setState { ConvertState.Loading }
                    is AsyncResult.Success -> {
                        val (from, to) = it.data
                        setState {
                            ConvertState.Content(
                                from = ConvertState.Amount(currency = from, value = "1"),
                                to = ConvertState.Amount(currency = to, value = "1")
                            )
                        }
                    }
                }
            }
            .launchIn(screenModelScope)
    }

    private fun changeFromAmount(event: ConvertEvent.FromAmountChange) {
        val state = state.value as? ConvertState.Content ?: return
        setState {
            state.copy(
                from = state.from.copy(value = event.value)
            )
        }
    }

    private fun changeToAmount(event: ConvertEvent.ToAmountChange) {
        val state = state.value as? ConvertState.Content ?: return
        setState {
            state.copy(
                to = state.to.copy(value = event.value)
            )
        }
    }

    private fun changeFromCurrency(event: ConvertEvent.SelectFromCurrency) =
        screenModelScope.launch {
            val state = stateContent ?: return@launch
            selectedCurrenciesRepository.putFrom(event.currency)
            setState { state.copy(from = state.from.copy(currency = event.currency)) }
        }

    private fun changeToCurrency(event: ConvertEvent.SelectToCurrency) =
        screenModelScope.launch {
            val state = stateContent ?: return@launch
            selectedCurrenciesRepository.putTo(event.currency)
            setState { state.copy(to = state.to.copy(currency = event.currency)) }
        }

    private fun handleFromCurrencyClick(event: ConvertEvent.OnFromCurrencyClick) {
        stateContent?.let {
            openCurrencyPicker(
                selectedCurrency = it.from.currency,
                isFrom = true
            )
        }
    }

    private fun handleToCurrencyClick(event: ConvertEvent.OnToCurrencyClick) {
        stateContent?.let {
            openCurrencyPicker(
                selectedCurrency = it.to.currency,
                isFrom = false
            )
        }
    }

    private fun openCurrencyPicker(selectedCurrency: Currency, isFrom: Boolean) {
        send(ConvertEffect.OpenCurrencyPicker(selectedCurrency, isFrom))
    }
}