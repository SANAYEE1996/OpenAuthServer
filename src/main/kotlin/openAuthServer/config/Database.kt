package openAuthServer.config

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object DatabaseConfig {
    fun init() {
        Database.connect(
            url = getConfigProperty("database.datasource.url"),
            user = getConfigProperty("database.datasource.username"),
            driver = getConfigProperty("database.datasource.driver-class-name"),
            password = getConfigProperty("database.datasource.password"),
        )
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) { block() }
}
