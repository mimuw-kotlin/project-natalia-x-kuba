plugins {
    // Apply the Kotlin JVM plugin from the version catalog.
    alias(libs.plugins.kotlin.jvm)

    // Apply the application plugin for building CLI applications.
    application
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use the Kotlin JUnit 5 integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    // Use the JUnit 5 engine.
    testImplementation(libs.junit.jupiter.engine)

    // JUnit platform launcher needed for tests.
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Application dependency
    implementation(libs.guava)

    // Coroutines dependency
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2")

    implementation("org.jline:jline:3.21.0")
}

// Java toolchain for environment compatibility.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

application {
    mainClass.set("com.GameOfLife.GameOfLifeKt")
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "com.GameOfLife.GameOfLifeKt" // Replace with your actual main class
        )
    }

    // Handle duplicate files
    duplicatesStrategy = DuplicatesStrategy.INCLUDE // or DuplicatesStrategy.EXCLUDE based on your need

    // Ensure dependencies are included in the JAR
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}