package domain.usecases

import domain.ports.UserRepository
import domain.ports.UserRepository.SaveResult.NewUser
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import validUser

class CreateUserTest {

    @Test
    fun `it calls the repo when saving a user`() {
        val user = validUser
        val repository = mockk<UserRepository> {
            every { save(user) } returns NewUser
        }
        val createUser = CreateUser(repository)

        createUser(user)

        verify(exactly = 1) { repository.save(user) }
    }
}