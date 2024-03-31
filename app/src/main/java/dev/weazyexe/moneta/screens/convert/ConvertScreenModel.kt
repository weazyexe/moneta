package dev.weazyexe.moneta.screens.convert

import dev.weazyexe.moneta.core.MonetaScreenModel
import dev.weazyexe.moneta.screens.convert.viewstate.ConvertViewState
import dev.weazyexe.moneta.screens.convert.viewstate.convertViewState

class ConvertScreenModel :
    MonetaScreenModel<ConvertState, ConvertViewState, ConvertEvent, ConvertEffect>(
        initialState = ConvertState.Loading,
        viewStateMapper = ::convertViewState
    ) {

    override fun sink(event: ConvertEvent) {
        when (event) {
            is ConvertEvent.FromAmountChange -> changeFromAmount(event)
            is ConvertEvent.ToAmountChange -> changeToAmount(event)

            is ConvertEvent.FromCurrencyClick -> {
                // handle from currency click
            }

            is ConvertEvent.ToCurrencyClick -> {
                // handle to currency click
            }
        }
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
}