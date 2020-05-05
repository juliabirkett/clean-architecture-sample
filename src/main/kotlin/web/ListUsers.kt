package web

import domain.User
import io.javalin.http.Context
import io.javalin.http.Handler
import usecases.ListUsers

class ListUsersHandler(private val listUsers: ListUsers) : Handler {

    override fun handle(ctx: Context) {
        ctx.json(listUsers().toRepresenter())
    }

    private fun List<User>.toRepresenter() =
        map { UserRepresenter(it.id, it.email.value, it.name) }

    private class UserRepresenter(val id: String?, val email: String, val name: String)
}