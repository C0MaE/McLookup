plugins {
    kotlin("jvm") version "2.2.0"
}

group = "dev.comae"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("org.json:json:20240303")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}