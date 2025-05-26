package openAuthServer.config

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*

fun getConfigProperty(path: String) = HoconApplicationConfig(ConfigFactory.load()).property(path).getString()