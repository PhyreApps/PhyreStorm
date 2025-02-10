plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.10"
    id("org.jetbrains.intellij.platform") version "2.2.1"
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.9"
}

group = "org.phyreapps"
version = "1.2-SNAPSHOT"


repositories {

    mavenCentral()
    google()
    maven { url = uri("https://cache-redirector.jetbrains.com/intellij-dependencies") }
    maven { url = uri("https://plugins.jetbrains.com/maven") }
    maven { url = uri("https://packages.jetbrains.team/maven/p/ij/intellij-dependencies") }

    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    implementation("org.jetbrains:annotations:24.0.0")

    intellijPlatform {
        phpstorm("2024.3.2.1", useInstaller = true)
        bundledPlugin("com.jetbrains.php")

        pluginVerifier()
        zipSigner()

//        testFramework(TestFrameworkType.Platform)
    }
}