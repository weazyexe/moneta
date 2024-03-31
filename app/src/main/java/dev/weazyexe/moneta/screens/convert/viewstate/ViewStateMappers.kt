package dev.weazyexe.moneta.screens.convert.viewstate

import dev.weazyexe.moneta.domain.model.Currency
import dev.weazyexe.moneta.screens.convert.ConvertState

fun convertViewState(state: ConvertState): ConvertViewState =
    when (state) {
        is ConvertState.Error -> ConvertViewState.Error
        is ConvertState.Loading -> ConvertViewState.Loading
        is ConvertState.Content -> state.toViewState()
    }

private fun ConvertState.Content.toViewState(): ConvertViewState.Content {
    return ConvertViewState.Content(
        from = from.toViewState(),
        to = to.toViewState()
    )
}

private fun ConvertState.Amount.toViewState(): AmountViewState {
    return AmountViewState(
        currency = currency.toViewState(),
        value = value
    )
}

private fun Currency.toViewState(): CurrencyViewState {
    return CurrencyViewState(
        emoji = emoji,
        code = code,
        symbol = symbol,
        title = title
    )
}