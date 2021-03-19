package domain.model

import domain.model.Password.InvalidPassword

data class Password(val hashed: String) {
    init {
        require(hashed.length > 4) { throw InvalidPassword() }
    }

    class InvalidPassword : Exception()
}

private object PasswordEncoder {
    fun encode(toEncode: String) = toEncode.hashCode().toString() // really bad encoding for the sake of example
}

fun String.toPassword(): Password = Password(PasswordEncoder.encode(this))