plugins {
    id("java")
    `kotlin-dsl`
}

group = "ru.vsu.cs.ereshkin_a_v"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
}

dependencies {
    testImplementation(platform(libs.junit))
    testImplementation(libs.junit.jupiter)
}

tasks.test {
    useJUnitPlatform()
}