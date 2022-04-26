package com.matryoshka.projectx.ui.common

import com.matryoshka.projectx.ui.validator.Validator
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@ExperimentalCoroutinesApi
class FieldStateTest {

    @Test
    fun `should change value`() {
        val expectedValue = "John"
        val fieldState = FieldState("")

        fieldState.onChange(expectedValue)

        assertEquals(expectedValue, fieldState.value)
    }

    @Test
    fun `should be valid if value is valid`() = runTest {
        val validator = mockk<Validator<String>>().apply {
            coEvery { validate(any()) } returns null
        }
        val fieldState = FieldState(initialValue = "", validators = listOf(validator))

        fieldState.validate()

        with(fieldState) {
            assertFalse(hasError)
            assertNull(error)
        }
    }

    @Test
    fun `should not be valid if value is not valid`() = runTest {
        val expectedError = RawStringError("Error occurred")
        val validator = mockk<Validator<String>>().apply {
            coEvery { validate(any()) } returns expectedError
        }
        val fieldState = FieldState(validators = listOf(validator), initialValue = "")

        fieldState.validate()

        with(fieldState) {
            assertTrue(hasError)
            assertEquals(expectedError, error)
        }
    }

    @Test
    fun `numValue should return null for not a number text`() {
        val fieldState = numberFieldState(1)

        fieldState.onChange("not number")

        assertNull(fieldState.numValue)
    }

    @Test
    fun `convert text to number if it is possible`() {
        val textNumber = "1"
        val fieldState = numberFieldState(null)

        fieldState.onChange(textNumber)

        assertEquals(1, fieldState.numValue)
    }
}