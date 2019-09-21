package app.listusers

import app.User
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("List users use case")
object UseCaseTest {

    @Test
    fun `GIVEN a list of users, WHEN requesting it, THEN it returns it`() {
        val repository = mockk<Repository> {
            every { list() } returns listOf(User("1", "email", "Luís", "Soares"))
        }

        val users = UseCase(repository).list()

        assertEquals(listOf(User("1", "email", "Luís", "Soares")), users)
    }
}