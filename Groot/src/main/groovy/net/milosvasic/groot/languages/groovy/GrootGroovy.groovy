package net.milosvasic.groot.languages.groovy

import org.gradle.api.Plugin
import org.gradle.api.Project


class GrootGroovy implements Plugin<Project> {

    void apply(Project project) {
        project.ext.groovyVersion = "2.4.9"
        project.apply(plugin: "java")
        project.apply(plugin: "groovy")
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
        project.sourceSets.main.java.srcDirs += 'build/generated-src/java'
        project.sourceSets.main.groovy.srcDirs += 'src/main/groovy'
        project.sourceSets.main.groovy.srcDirs += 'build/generated-src/groovy'
        project.sourceSets.test.java.srcDirs += 'src/test/java'
        project.sourceSets.test.groovy.srcDirs += 'src/test/groovy'
    }

}
