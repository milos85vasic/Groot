package net.milosvasic.groot.languages.scala

import org.gradle.api.Plugin
import org.gradle.api.Project


class GrootScala implements Plugin<Project> {

    void apply(Project project) {
        project.ext.scalaVersion = "2.12.1"
        project.ext.scalaTestVersion = "3.0.1"
        project.apply(plugin: "java")
        project.apply(plugin: "scala")
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
        project.sourceSets.main.scala.srcDirs += 'src/main/scala'
        project.sourceSets.main.scala.srcDirs += 'build/generated-src/scala'
        project.sourceSets.test.java.srcDirs += 'src/test/java'
        project.sourceSets.test.scala.srcDirs += 'src/test/scala'
    }

}
