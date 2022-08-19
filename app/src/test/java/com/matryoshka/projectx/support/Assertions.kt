package com.matryoshka.projectx.support

import com.matryoshka.projectx.exception.AppException
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

fun AppException?.assert(expected: AppException) {
    assertNotNull(this)
    assertEquals(expected::class.qualifiedName, this::class.qualifiedName)
    assertEquals(expected.message, message)
}