package repository.mysql

import com.wix.mysql.EmbeddedMysql
import com.wix.mysql.EmbeddedMysql.anEmbeddedMysql
import com.wix.mysql.config.MysqldConfig.aMysqldConfig
import com.wix.mysql.distribution.Version
import domain.UserEntity
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.count
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import repository.mysql.UserRepository.Users

@DisplayName("Create user repository")
object CreateUserRepositoryTest {

    private lateinit var dbServer: EmbeddedMysql
    private lateinit var userRepository: UserRepository
    private lateinit var dbClient: Database

    @BeforeAll
    @JvmStatic
    fun beforeAll() {
        val config = aMysqldConfig(Version.v5_7_latest).withPort(3302).withUser("user", "pass").build()
        dbServer = anEmbeddedMysql(config).addSchema("test_schema").start()
        dbClient = Database.connect("jdbc:mysql://user:pass@localhost:3302/test_schema", "com.mysql.cj.jdbc.Driver")
        userRepository = UserRepository(dbClient)
        userRepository.createSchema()
    }

    @BeforeEach
    fun beforeEach() {
        userRepository.deleteAll()
    }

    @Test
    fun `GIVEN a user, WHEN storing it, THEN it's persisted and gets an id`() {
        val user = UserEntity(email = "lsoares@gmail.com", name = "Luís Soares", hashedPassword = "hashed")

        userRepository.save(user)

        val row = transaction(dbClient) {
            Users.select { Users.email eq user.email }.first()
        }
        assertEquals(
            user,
            UserEntity(email = row[Users.email], hashedPassword = row[Users.hashedPassword], name = row[Users.name])
        )
        assertTrue(row[Users.id].value > 0)
    }

    @Test
    fun `GIVEN an existing user, WHEN storing it, THEN it's not persisted and an exception is thrown`() {
        val user = UserEntity(email = "lsoares@gmail.com", name = "Luís Soares", hashedPassword = "hashed")

        userRepository.save(user)
        assertThrows<UserEntity.UserAlreadyExists> {
            UserRepository(dbClient).save(user)
        }

        transaction(dbClient) {
            Users.slice(Users.email.count()).select { Users.email eq user.email }.first().run {
                assertEquals(1, this[Users.email.count()])
            }
        }
    }

    @AfterAll
    @JvmStatic
    fun afterAll() = dbServer.stop()
}