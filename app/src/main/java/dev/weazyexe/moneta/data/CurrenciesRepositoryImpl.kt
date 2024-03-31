package dev.weazyexe.moneta.data

import android.content.Context
import dev.weazyexe.moneta.domain.model.Currency
import dev.weazyexe.moneta.domain.repository.CurrenciesRepository
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class CurrenciesRepositoryImpl(
    private val context: Context,
    private val json: Json
) : CurrenciesRepository {

    private var cached: List<Currency>? = null

    override suspend fun get(): List<Currency> {
        cached?.let { return it }

        val rawCurrencies = context.assets.open(CURRENCIES_FILE).use { inputStream ->
            val rawCurrenciesJson = inputStream.bufferedReader().use { it.readText() }
            json.decodeFromString<List<CurrencyDto>>(rawCurrenciesJson)
        }
        return rawCurrencies
            .map { it.toCurrency() }
            .also { cached = it }
    }

    private companion object {

        private const val CURRENCIES_FILE = "currencies.json"
    }
}

@Serializable
private class CurrencyDto(
    @SerialName("symbol")
    val symbol: String,

    @SerialName("name")
    val name: String,

    @SerialName("code")
    val code: String,

    @SerialName("emoji")
    val emoji: String
) {

    fun toCurrency() = Currency(
        symbol = symbol,
        name = name,
        code = code,
        emoji = emoji
    )
}