package net.milosvasic.groot.languages.kotlin

import org.gradle.api.Plugin
import org.gradle.api.Project

class GrootKotlin implements Plugin<Project> {

    void apply(Project project) {
        String version = "1.1.0"
        project.apply(plugin: "java")
        project.apply(plugin: "kotlin")
        project.buildscript {
            repositories {
                jcenter()
                mavenCentral()
            }
        }
        project.repositories {
            jcenter()
            mavenCentral()
        }
        project.dependencies {
            compile "junit:junit:4.12"
            testCompile "junit:junit:4.12"

            compile "org.jetbrains.kotlin:kotlin-stdlib:$version"
            compile "org.jetbrains.kotlin:kotlin-reflect:$version"

            testCompile "org.jetbrains.kotlin:kotlin-test:$version"
            testCompile "org.jetbrains.kotlin:kotlin-test-junit:$version"
        }
        project.sourceSets.main.kotlin.srcDirs += 'src/main/java'
        project.sourceSets.main.kotlin.srcDirs += 'src/main/kotlin'
        project.sourceSets.main.kotlin.srcDirs += 'build/generated-src/java'
        project.sourceSets.main.kotlin.srcDirs += 'build/generated-src/kotlin'
    }

}