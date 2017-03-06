package net.milosvasic.groot.languages.scala

import org.gradle.api.Plugin
import org.gradle.api.Project


class GrootScala implements Plugin<Project> {

    public String version = "2.12.1"
    public String testVersion = "3.0.1"

    void apply(Project project) {
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
        project.dependencies {
            compile "junit:junit:4.12"
            testCompile "junit:junit:4.12"

            compile "org.scala-lang:scala-library:$version"
            testCompile "org.scala-lang:scala-library:$version"
            testCompile "org.scalatest:scalatest_2.11:$testVersion"
        }
        project.sourceSets.main.java.srcDirs += 'src/main/java'
        project.sourceSets.main.java.srcDirs += 'build/generated-src/java'
        project.sourceSets.main.scala.srcDirs += 'src/main/scala'
        project.sourceSets.main.scala.srcDirs += 'build/generated-src/scala'
        project.sourceSets.test.java.srcDirs += 'test/main/java'
        project.sourceSets.test.scala.srcDirs += 'test/main/scala'
    }

}
