package dev.weazyexe.moneta.core

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * Does not produce the same value in a raw, so respect "distinct until changed emissions"
 * */
class DerivedStateFlow<T>(
    private val getValue: () -> T,
    private val flow: Flow<T>
) : StateFlow<T> {

    override val replayCache: List<T>
        get() = listOf(value)

    override val value: T
        get() = getValue()

    override suspend fun collect(collector: FlowCollector<T>): Nothing {
        coroutineScope { flow.distinctUntilChanged().stateIn(this).collect(collector) }
    }
}

fun <T1, R> StateFlow<T1>.mapState(transform: (a: T1) -> R): StateFlow<R> {
    return DerivedStateFlow(
        getValue = { transform(this.value) },
        flow = this.map { a -> transform(a) }
    )
}