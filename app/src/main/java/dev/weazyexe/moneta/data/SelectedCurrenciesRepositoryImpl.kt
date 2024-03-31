package dev.weazyexe.moneta.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.weazyexe.moneta.domain.model.Currency
import dev.weazyexe.moneta.domain.repository.CurrenciesRepository
import dev.weazyexe.moneta.domain.repository.SelectedCurrenciesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SelectedCurrenciesRepositoryImpl(
    override val currenciesRepository: CurrenciesRepository,
    private val context: Context
) : SelectedCurrenciesRepository {

    private val Context.dataStore: DataStore<Preferences>
            by preferencesDataStore(name = SELECTED_CURRENCIES_FILE)

    override suspend fun getFrom(): Currency {
        val code = get(FROM_CURRENCY_KEY) ?: return getDefaultFrom()
        return currenciesRepository.get().first { it.code == code }
    }

    override suspend fun getTo(): Currency {
        val code = get(TO_CURRENCY_KEY) ?: return getDefaultTo()
        return currenciesRepository.get().first { it.code == code }
    }

    override suspend fun putFrom(currency: Currency) {
        set(FROM_CURRENCY_KEY, currency.code)
    }

    override suspend fun putTo(currency: Currency) {
        set(TO_CURRENCY_KEY, currency.code)
    }

    private suspend fun get(key: Preferences.Key<String>): String? =
        context.dataStore.data
            .map { preferences -> preferences[key] }
            .first()

    private suspend fun set(key: Preferences.Key<String>, value: String) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    companion object {

        private const val SELECTED_CURRENCIES_FILE = "selected_currencies"

        private val FROM_CURRENCY_KEY = stringPreferencesKey("from_currency")
        private val TO_CURRENCY_KEY = stringPreferencesKey("to_currency")
    }
}