package net.milosvasic.groot.languages.kotlin

import org.gradle.api.Plugin
import org.gradle.api.Project

class GrootKotlin implements Plugin<Project> {

    void apply(Project project) {
        project.ext.kotlinVersion = "1.1.0"
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
        project.sourceSets.main.java.srcDirs += 'src/main/java'
        project.sourceSets.main.kotlin.srcDirs += 'src/main/kotlin'
        project.sourceSets.main.java.srcDirs += 'build/generated-src/java'
        project.sourceSets.main.kotlin.srcDirs += 'build/generated-src/kotlin'
        project.sourceSets.test.java.srcDirs += 'src/test/java'
        project.sourceSets.test.kotlin.srcDirs += 'src/test/kotlin'
    }

}