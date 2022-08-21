package com.checkers.utlis

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

suspend inline fun <T, R> Iterable<T>.asyncMap(scope: CoroutineScope, crossinline block: suspend (T) -> R) =
    map { scope.async { block(it) } }.awaitAll()

suspend inline fun <T, R> Iterable<T>.asyncMapIndexed(scope: CoroutineScope, crossinline block: suspend (Int, T) -> R) =
    mapIndexed {index, it -> scope.async { block(index, it) } }.awaitAll()