package dev.weazyexe.moneta.screens.currencies

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel

data object CurrenciesScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<CurrenciesScreenModel>()
        val state by screenModel.viewState.collectAsState()

        CurrenciesBody(state)
    }
}