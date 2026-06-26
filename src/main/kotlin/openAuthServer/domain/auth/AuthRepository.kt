package openAuthServer.domain.auth

import openAuthServer.config.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class AuthRepository {
    private fun resultRowToArticle(row: ResultRow) = UserInfo(
        sub = row[User.sub],
        email = row[User.email],
        name = row[User.name],
        picture = ""
    )

    suspend fun insertUser(data: UserInfo) : UserInfo ? = dbQuery {
        val insertStatement = User.insert {
            it[sub] = data.sub.orEmpty()
            it[email] = data.email
            it[name] = data.name
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToArticle)
    }

    suspend fun findUser(email:String): UserInfo ? = dbQuery {
        User.select { User.email eq email }
            .map(::resultRowToArticle)
            .singleOrNull()
    }
}