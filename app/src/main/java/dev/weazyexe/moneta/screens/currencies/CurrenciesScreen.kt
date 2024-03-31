package dev.weazyexe.moneta.screens.currencies

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import dev.weazyexe.moneta.domain.model.Currency

class CurrenciesScreen(
    private val selectedCurrency: Currency? = null
) : Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<CurrenciesScreenModel>()
        val state by screenModel.viewState.collectAsState()

        LaunchedEffect(selectedCurrency) {
            screenModel.sink(CurrenciesEvent.SelectCurrency(selectedCurrency))
        }

        CurrenciesBody(
            state = state,
            eventSink = screenModel::sink,
            effects = screenModel.effects
        )
    }
}