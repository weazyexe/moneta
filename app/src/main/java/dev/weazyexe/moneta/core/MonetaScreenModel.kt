package dev.weazyexe.moneta.core

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class MonetaScreenModel<State, ViewState, UiEvent, UiEffect>(
    initialState: State,
    viewStateMapper: (State) -> ViewState
) : ScreenModel {

    private val _state: MutableStateFlow<State> = MutableStateFlow(initialState)
    protected val state: StateFlow<State> = _state.asStateFlow()

    val viewState: StateFlow<ViewState> = state.mapState { viewStateMapper(it) }

    private val _effects = Channel<UiEffect>(Channel.BUFFERED)
    val effects: Flow<UiEffect> = _effects.receiveAsFlow()

    abstract fun sink(event: UiEvent)

    fun send(effect: UiEffect) {
        screenModelScope.launch {
            _effects.send(effect)
        }
    }

    fun setState(reducer: State.() -> State) {
        _state.value = _state.value.reducer()
    }

    fun <T> async(block: suspend () -> T): Flow<AsyncResult<T>> = flow {
        emit(AsyncResult.Loading())
        try {
            val data = block()
            emit(AsyncResult.Success(data))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(AsyncResult.Error(e))
        }
    }.flowOn(Dispatchers.IO)
}