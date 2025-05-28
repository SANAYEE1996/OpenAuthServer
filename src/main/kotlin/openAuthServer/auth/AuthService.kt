package openAuthServer.auth

import openAuthServer.config.DatabaseConfig.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert

class AuthService(){

    private fun resultRowToArticle(row: ResultRow) = GoogleUserInfo(
        sub = row[User.sub],
        email = row[User.email],
        name = row[User.name],
        picture = ""
    )

    suspend fun addTicket(data: GoogleUserInfo):GoogleUserInfo ? = dbQuery {
        val insertStatement = User.insert {
            it[sub] = data.sub
            it[email] = data.email.orEmpty()
            it[name] = data.name.orEmpty()
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToArticle)
    }
}