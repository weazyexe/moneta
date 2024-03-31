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
        val value: String
    )
}

sealed interface ConvertEvent {

    data class FromAmountChange(val value: String) : ConvertEvent

    data class ToAmountChange(val value: String) : ConvertEvent

    data object OnFromCurrencyClick : ConvertEvent

    data object OnToCurrencyClick : ConvertEvent

    data class SelectFromCurrency(val currency: Currency) : ConvertEvent

    data class SelectToCurrency(val currency: Currency) : ConvertEvent
}

sealed interface ConvertEffect {

    data class OpenCurrencyPicker(
        val selectedCurrency: Currency,
        val isFrom: Boolean
    ) : ConvertEffect
}