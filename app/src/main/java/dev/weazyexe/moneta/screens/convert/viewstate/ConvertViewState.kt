package dev.weazyexe.moneta.screens.convert.viewstate

import androidx.compose.runtime.Immutable

@Immutable
sealed interface ConvertViewState {

    data object Loading : ConvertViewState

    data object Error : ConvertViewState

    data class Content(
        val from: AmountViewState,
        val to: AmountViewState
    ) : ConvertViewState
}

data class AmountViewState(
    val currency: CurrencyViewState,
    val value: String
)

data class CurrencyViewState(
    val emoji: String,
    val code: String,
    val symbol: String,
    val title: String
)