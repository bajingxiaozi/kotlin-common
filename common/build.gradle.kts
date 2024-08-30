import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    // google
    implementation("com.google.code.gson:gson:2.10")

    // third party
    implementation("commons-io:commons-io:2.16.1")
    implementation("org.json:json:20231013")
    implementation("org.apache.commons:commons-text:1.11.0")
}