package dev.weazyexe.moneta.core

import androidx.compose.runtime.Immutable

@Immutable
sealed interface AsyncResult<T> {

    @Immutable
    data class Success<T>(val data: T) : AsyncResult<T>

    @Immutable
    data class Error<T>(val throwable: Throwable) : AsyncResult<T>

    @Immutable
    class Loading<T> : AsyncResult<T>
}

fun <T, F> AsyncResult<T>.map(transform: (T) -> F): AsyncResult<F> {
    return when (this) {
        is AsyncResult.Success -> AsyncResult.Success(transform(data))
        is AsyncResult.Error -> AsyncResult.Error(throwable)
        is AsyncResult.Loading -> AsyncResult.Loading()
    }
}