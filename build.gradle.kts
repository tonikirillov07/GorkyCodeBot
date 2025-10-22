plugins {
    id("java")
}

group = "org.ds"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("ch.qos.logback:logback-classic:1.5.20")

    implementation("org.springframework:spring-core:7.0.0-M9")
    implementation("org.springframework:spring-context:7.0.0-M9")
    implementation("org.springframework:spring-beans:7.0.0-M9")

    implementation("org.jetbrains:annotations:26.0.2-1")

    implementation("com.github.pengrad:java-telegram-bot-api:9.2.0")
    implementation("chat.giga:gigachat-java:0.1.10")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}