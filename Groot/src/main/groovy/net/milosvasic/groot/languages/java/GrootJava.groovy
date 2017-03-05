package net.milosvasic.groot.languages.java

import org.gradle.api.Plugin
import org.gradle.api.Project


class GrootJava implements Plugin<Project> {

    void apply(Project project) {
        project.apply(plugin: "java")
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
        }
        project.sourceSets.main.java.srcDirs += 'src/main/java'
        project.sourceSets.main.java.srcDirs += 'build/generated-src/java'
    }

}
