package com.checkers

import kotlin.test.assertEquals
import kotlin.test.assertTrue

fun assertEqualLists(expected: List<Any?>, result: List<Any?>) {
    assertEquals(expected.size, result.size)
    assertTrue(expected.containsAll(result))
    assertTrue(result.containsAll(expected))
}