package domain.model

import domain.model.Password.InvalidPassword
import invalidPasswordHash
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import validPassword

class PasswordTest {

    @Test
    fun `do not create invalid passwords`() {
        assertThrows<InvalidPassword> {
            validPassword.copy(hashed = invalidPasswordHash)
        }
    }

    @Test
    fun `do not convert to invalid passwords`() {
        assertThrows<InvalidPassword> { invalidPasswordHash.toPassword() }
    }

    @Test
    fun `encodes a string`() {
        assertNotEquals("abcde", "abcde".toPassword().hashed)
    }

    @Test
    fun `encodes deterministically`() {
        assertEquals(validPassword.hashed, validPassword.hashed)
    }
}