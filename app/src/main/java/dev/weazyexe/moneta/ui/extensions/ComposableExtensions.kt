package dev.weazyexe.moneta.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun <E> ReceiveEffect(effects: Flow<E>, block: suspend E.() -> Unit) {
    LaunchedEffect(Unit) {
        effects
            .onEach { block(it) }
            .launchIn(this)
    }
}