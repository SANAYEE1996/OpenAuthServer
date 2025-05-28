package openAuthServer.auth

import org.jetbrains.exposed.sql.Table

data class GoogleUserInfo(
    val sub: String,
    val name: String?,
    val email: String?,
    val picture: String?,
)

object User : Table(){
    private val id = integer("id").autoIncrement()
    val sub = varchar("sub", 100)
    val email = varchar("email", 30)
    val name = varchar("name", 50)

    override val primaryKey = PrimaryKey(id)
}