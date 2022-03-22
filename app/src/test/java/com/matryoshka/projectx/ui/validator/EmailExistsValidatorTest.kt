package com.matryoshka.projectx.ui.validator

import com.matryoshka.projectx.R
import com.matryoshka.projectx.service.AuthService
import com.matryoshka.projectx.ui.common.StringResourceError
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

@ExperimentalCoroutinesApi
class EmailExistsValidatorTest {

    @Test
    fun `email should not be valid if exists`() = runTest {
        val email = "john@gmail.com"
        val expectedError = StringResourceError(R.string.email_exists)
        val authService = mockk<AuthService>().apply {
            coEvery { checkEmailExists(email) } returns true
        }
        val validator = EmailExistsValidator(authService)

        val result = validator.validate(email)

        assertEquals(expectedError, result)
    }

    @Test
    fun `email should be valid if doesn't exist`() = runTest {
        val email = "john@gmail.com"
        val authService = mockk<AuthService>().apply {
            coEvery { checkEmailExists(email) } returns false
        }
        val validator = EmailExistsValidator(authService)

        val result = validator.validate(email)

        assertNull(result)
    }
}