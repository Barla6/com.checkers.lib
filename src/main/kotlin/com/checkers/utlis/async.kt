package com.checkers.utlis

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

suspend inline fun <T, R> Iterable<T>.asyncMap(scope: CoroutineScope, crossinline block: (T) -> R) =
    map { scope.async { block(it) } }.awaitAll()