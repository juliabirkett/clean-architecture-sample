package domain.model

import domain.model.User.InvalidUser
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import validUser

class UserTest {

    @Test
    fun `creates a user`() {
        assertDoesNotThrow { validUser }
    }

    @Test
    fun `do not create users with invalid names`() {
        assertThrows<InvalidUser> { validUser.copy(name = "J") }
    }
}