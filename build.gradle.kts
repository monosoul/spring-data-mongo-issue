import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.3"
    id("io.spring.dependency-management") version "1.0.13.RELEASE"
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.spring") version "1.7.10"
}

group = "dev.monosoul.spring.data.mongo"
version = "0.0.1-SNAPSHOT"
val javaVersion = JavaVersion.VERSION_17
java.sourceCompatibility = javaVersion

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(platform("org.testcontainers:testcontainers-bom:1.17.3"))
    testImplementation("org.testcontainers:mongodb")
    testImplementation("io.strikt:strikt-jvm:0.34.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "$javaVersion"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
