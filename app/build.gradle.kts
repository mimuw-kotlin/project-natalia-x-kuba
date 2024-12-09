plugins {
    alias(libs.plugins.kotlin.jvm)
    application
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
}

repositories {
    mavenCentral()
}

dependencies {
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation(libs.guava)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2")
    implementation("org.jline:jline:3.21.0")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

application {
    mainClass.set("com.gameOfLife.GameOfLifeKt")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "com.gameOfLife.GameOfLifeKt",
        )
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}
