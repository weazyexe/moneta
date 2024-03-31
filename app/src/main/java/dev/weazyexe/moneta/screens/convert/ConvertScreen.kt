package dev.weazyexe.moneta.screens.convert

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel

class ConvertScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<ConvertScreenModel>()
        val state by screenModel.viewState.collectAsState()

        ConvertBody(
            state = state,
            eventSink = screenModel::sink,
            effects = screenModel.effects
        )
    }


}