// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.3" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
}
buildscript{
    val kotlin_version by extra("1.9.22")
    dependencies{
        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:com.google.android.libraries.mapsplatform.secrets-gradle-plugin.gradle.plugin:2.0.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
    repositories {
        mavenCentral()
    }
}