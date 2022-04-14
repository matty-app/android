package com.matryoshka.projectx.support

import com.matryoshka.projectx.exception.ProjectxException
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

fun ProjectxException?.assert(expected: ProjectxException) {
    assertNotNull(this)
    assertEquals(expected::class.qualifiedName, this::class.qualifiedName)
    assertEquals(expected.message, message)
}