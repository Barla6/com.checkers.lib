package com.checkers.utlis

fun assert(block: () -> Unit) = try {
    block()
    true
} catch (e: Throwable) {
    false
}