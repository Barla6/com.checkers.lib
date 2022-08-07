
val kotlinVersion = "5.8.2"
val logbackVersion = "1.2.11"

plugins {
    application
    kotlin("jvm") version "1.6.20"
}

group = "com.checkers"
version = "0.0.1"
application {
    mainClass.set("com.checkers.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

dependencies {
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$kotlinVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$kotlinVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.6.21")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.3")
    implementation("com.google.code.gson:gson:2.7")
}

tasks.test {
    useJUnitPlatform()
}
