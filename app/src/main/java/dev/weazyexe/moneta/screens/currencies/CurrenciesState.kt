package dev.weazyexe.moneta.screens.currencies

import dev.weazyexe.moneta.core.AsyncResult
import dev.weazyexe.moneta.domain.model.Currency

data class CurrenciesState(
    val currencies: AsyncResult<List<Currency>> = AsyncResult.Loading()
)