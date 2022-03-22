package com.matryoshka.projectx.ui.validator

import com.matryoshka.projectx.R
import com.matryoshka.projectx.ui.common.StringResourceError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

@ExperimentalCoroutinesApi
class NameValidatorTest {

    @Test
    fun `name should be valid`() = runTest {
        val name = "John"
        val validator = NameValidator()

        val result = validator.validate(name)

        assertNull(result)
    }

    @Test
    fun `empty name should not be valid`() = runTest {
        val expectedError = StringResourceError(R.string.name_required)
        val name = ""
        val validator = NameValidator()

        val result = validator.validate(name)

        assertEquals(expectedError, result)
    }

    @Test
    fun `null name should not be valid`() = runTest {
        val expectedError = StringResourceError(R.string.name_required)
        val name = null
        val validator = NameValidator()

        val result = validator.validate(name)

        assertEquals(expectedError, result)
    }
}