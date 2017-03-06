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

            compile "org.scalatest:scalatest_${getVersionForTest()}:$testVersion"
            testCompile "org.scalatest:scalatest_${getVersionForTest()}:$testVersion"
        }
        project.sourceSets.main.java.srcDirs += 'src/main/java'
        project.sourceSets.main.java.srcDirs += 'build/generated-src/java'
        project.sourceSets.main.scala.srcDirs += 'src/main/scala'
        project.sourceSets.main.scala.srcDirs += 'build/generated-src/scala'
        project.sourceSets.test.java.srcDirs += 'src/test/java'
        project.sourceSets.test.scala.srcDirs += 'src/test/scala'
    }

    private String getVersionForTest() {
        if (version.count(".") >= 2) {
            int index = version.indexOf(".")
            return version.substring(0, index + 3)
        }
        return version
    }

}
