val exposedVersion: String by project
val mySqlVersion: String by project
val ktorVersion: String by project

plugins {
    kotlin("jvm") version "2.1.0"
    id("io.ktor.plugin") version "3.1.3"
    // 🌟 [Fix 1] Kotlin JVM 버전(2.1.0)과 완벽하게 싱크를 맞춰줍니다!
    kotlin("plugin.serialization") version "2.1.0"
    id("org.jlleitschuh.gradle.ktlint") version "12.3.0"
}

group = "com.auth"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    // 🌟 [Ktor Server Core] 플러그인이 버전을 관리해주므로 뒤에 버전을 굳이 안 붙여도 정갈하게 관리됩니다.
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-auth")
    implementation("io.ktor:ktor-server-auth-jwt-jvm")
    implementation("io.ktor:ktor-server-cors")
    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-server-status-pages")
    implementation("io.ktor:ktor-server-call-logging-jvm")
    implementation("io.ktor:ktor-server-content-negotiation")

    // 🌟 [Ktor Client Core] 클라이언트 라이브러리들도 깔끔하게 통일!
    implementation("io.ktor:ktor-client-core")
    implementation("io.ktor:ktor-client-apache")
    implementation("io.ktor:ktor-client-cio")
    implementation("io.ktor:ktor-client-logging")
    implementation("io.ktor:ktor-client-content-negotiation")

    // Serialization & Logging
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("com.sksamuel.hoplite:hoplite-hocon:2.8.2")

    // Database (Exposed & MySQL)
    implementation("mysql:mysql-connector-java:$mySqlVersion")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")

    // 🌟 [Fix 2] 중복되던 Koin 3.5.6은 과감히 지우고,
    // Ktor 3.x를 완벽 지원하는 Koin 4.0.0 안정 버전으로 깔끔하게 통합합니다!
    implementation("io.insert-koin:koin-ktor:4.0.0")
    implementation("io.insert-koin:koin-logger-slf4j:4.0.0")

    // Testing
    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation("org.jetbrains.kotlin:kotlin-test:2.1.0") // 여기도 2.1.0 싱크 맞춤
}