package domain.usecases

import domain.model.Email
import domain.model.User
import domain.model.toPassword
import domain.ports.UserRepository
import io.mockk.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Create user use case")
class CreateUserTest {

    @Test
    fun `it calls the repo when saving a user`() {
        val user = User(email = Email("luis.s@gmail.com"), name = "Luís Soares", password = "toEncode".toPassword())
        val repository = mockk<UserRepository> {
            every { save(user) } just Runs
        }
        val createUser = CreateUser(repository)

        createUser(user)

        verify(exactly = 1) { repository.save(user) }
    }
}