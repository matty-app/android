package com.matryoshka.projectx.ui.validator

import com.matryoshka.projectx.R
import com.matryoshka.projectx.ui.common.StringResourceError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNull
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class EmailValidatorsTest {

    @Test
    fun `email should be valid`() = runTest {
        val email = "john@gmail.com"
        val validator = EmailValidator()

        val result = validator.validate(email)

        assertNull(result)
    }

    @Test
    fun `email should not be valid if has bad format`() = runTest {
        val email = "john@com"
        val expectedError = StringResourceError(R.string.valid_email_required)
        val validator = EmailValidator()

        val result = validator.validate(email)

        assertEquals(expectedError, result)
    }

    @Test
    fun `empty email should not be valid`() = runTest {
        val email = ""
        val expectedError = StringResourceError(R.string.valid_email_required)
        val validator = EmailValidator()

        val result = validator.validate(email)

        assertEquals(expectedError, result)
    }

    @Test
    fun `null email should not be valid`() = runTest {
        val email = null
        val expectedError = StringResourceError(R.string.valid_email_required)
        val validator = EmailValidator()

        val result = validator.validate(email)

        assertEquals(expectedError, result)
    }
}