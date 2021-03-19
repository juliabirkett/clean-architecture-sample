import domain.model.*
import io.github.serpro69.kfaker.Faker
import io.github.serpro69.kfaker.FakerConfig
import io.github.serpro69.kfaker.create
import io.github.serpro69.kfaker.provider.Internet

private val fakerConfig = FakerConfig.builder().create {
    locale = "en"
}
private val faker by lazy { Faker(fakerConfig) }

val invalidPasswordHash: String = faker.randomProvider.randomClassInstance {
    typeGenerator { faker.pokemon.names().takeIf { it.length < 4 } ?: "123" }
}

val validPassword: Password = faker.randomProvider.randomClassInstance {
    typeGenerator { faker.pokemon.names().takeIf { it.length > 4 } ?: "valid-password" }
}

val validUser: User = User(
    id = faker.idNumber.toString().toUserId(),
    name = faker.name.neutralFirstName().takeIf { it.length >= 2 } ?: "Alice",
    email = faker.internet.validEmail().toEmail(),
    password = validPassword
)

fun Internet.validEmail(name: String = ""): String = email(name.replace("[à-úÀ-Ú0-9]".toRegex(), ""))