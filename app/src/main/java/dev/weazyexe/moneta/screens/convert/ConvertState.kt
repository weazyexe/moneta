package dev.weazyexe.moneta.screens.convert

import dev.weazyexe.moneta.domain.model.Currency

sealed interface ConvertState {

    data object Loading : ConvertState

    data object Error : ConvertState

    data class Content(
        val from: Amount,
        val to: Amount
    ) : ConvertState

    data class Amount(
        val currency: Currency,
        val value: Double
    )
}

sealed interface ConvertEvent {

    data class FromAmountChange(val value: Double) : ConvertEvent

    data class ToAmountChange(val value: Double) : ConvertEvent

    data object FromCurrencyClick : ConvertEvent

    data object ToCurrencyClick : ConvertEvent
}

sealed interface ConvertEffect {

//    data class OpenCurrencyPicker
}