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

    implementation("org.hibernate.orm:hibernate-community-dialects:7.2.0.CR1")
    implementation("org.hibernate.orm:hibernate-core:7.2.0.CR1")
    implementation("org.xerial:sqlite-jdbc:3.50.3.0")

    implementation("org.jetbrains:annotations:26.0.2-1")

    implementation("com.github.pengrad:java-telegram-bot-api:9.2.0")
    implementation("chat.giga:gigachat-java:0.1.10")

    testImplementation("org.springframework:spring-test:7.0.0-RC2")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes(mapOf("Main-Class" to "org.ds.Main"))
    }
}