plugins {
    kotlin("jvm") version "2.2.0"
    `maven-publish`
}

group = "dev.comae"
version = "1.0.1"

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

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            groupId = "dev.comae"
            artifactId = "McLookup"
            version = version

            pom {
                name.set("McLookup")
                description.set("A very simple Minecraft Lookup library")
                url.set("https://github.com/C0MaE/McLookup")
            }
        }
    }

    repositories {
        maven {
            name = "OwnServer"
            url = uri("https://repo.comae.dev/repository/maven-releases/") // oder snapshot repo

            credentials {
                username = findProperty("mavenUser") as String? ?: System.getenv("MAVEN_USERNAME")
                password = findProperty("mavenPassword") as String? ?: System.getenv("MAVEN_PASSWORD")
            }
        }
    }
}