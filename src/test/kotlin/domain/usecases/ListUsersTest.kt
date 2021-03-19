package domain.usecases

import domain.ports.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import validUser

class ListUsersTest {

    @Test
    fun `it returns a list of users from the repo`() {
        val user = validUser
        val repository = mockk<UserRepository> {
            every { findAll() } returns listOf(user)
        }
        val listUsers = ListUsers(repository)

        val users = listUsers()

        verify(exactly = 1) { repository.findAll() }
        assertEquals(
            listOf(user),
            users
        )
    }
}