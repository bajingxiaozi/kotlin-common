plugins {
    kotlin("jvm")
}

group = "me.youfang"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    //
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

    // google
    implementation("com.google.code.gson:gson:2.10")

    // third party
    implementation("commons-io:commons-io:2.16.1")
    implementation("org.json:json:20231013")
    implementation("org.apache.commons:commons-text:1.11.0")
}

kotlin {
    jvmToolchain(18)
}