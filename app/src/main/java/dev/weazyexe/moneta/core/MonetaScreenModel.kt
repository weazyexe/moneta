package dev.weazyexe.moneta.core

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

abstract class MonetaScreenModel<S, VS>(
    initialState: S,
    viewStateMapper: (S) -> VS
) : ScreenModel {

    private val _state: MutableStateFlow<S> = MutableStateFlow(initialState)
    protected val state: StateFlow<S> = _state.asStateFlow()

    val viewState: StateFlow<VS> = state.mapState { viewStateMapper(it) }

    fun setState(reducer: S.() -> S) {
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