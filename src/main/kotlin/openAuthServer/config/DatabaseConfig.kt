package openAuthServer.config

import io.ktor.server.application.Application
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.koin.ktor.ext.get

fun Application.configureDatabases(){

    val appConfig = get<AppConfig>()
    val dbProps = appConfig.database.datasource

    Database.connect(
        url = dbProps.url,
        user = dbProps.username,
        driver = dbProps.driverClassName,
        password = dbProps.password,
    )
}

suspend fun <T> dbQuery(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }