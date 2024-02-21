plugins {
    kotlin("jvm") version "1.9.22"
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

group = "com.mesutpiskin.keycloak"
version = "1.0.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compileOnly("org.keycloak:keycloak-server-spi:23.0.3")
    compileOnly("org.keycloak:keycloak-server-spi-private:23.0.3")
    compileOnly("org.keycloak:keycloak-services:23.0.3")
    compileOnly("org.projectlombok:lombok:1.18.22")
    compileOnly("com.google.auto.service:auto-service:1.0.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.9.22")
    compileOnly("org.jboss.logging:jboss-logging:3.5.3.Final")
}

tasks.withType<Jar> {
    manifest {
        attributes["Implementation-Title"] = "Keycloak 2FA Email Authenticator"
        attributes["Implementation-Version"] = version
    }
    from(sourceSets.main.get().output)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    archiveClassifier.set("")
}

tasks.build {
    dependsOn(tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>())
}