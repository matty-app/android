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
class InputFieldTest {

    @Test
    fun `should change value`() {
        val expectedValue = "John"
        val inputField = InputField<String>()

        inputField.onChange(expectedValue)

        assertEquals(expectedValue, inputField.value)
    }

    @Test
    fun `should be valid if value is valid`() = runTest {
        val validator = mockk<Validator<String>>().apply {
            coEvery { validate(any()) } returns null
        }
        val inputField = InputField(validators = listOf(validator))

        inputField.validate()

        with(inputField) {
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
        val inputField = InputField(validators = listOf(validator))

        inputField.validate()

        with(inputField) {
            assertTrue(hasError)
            assertEquals(expectedError, error)
        }
    }
}