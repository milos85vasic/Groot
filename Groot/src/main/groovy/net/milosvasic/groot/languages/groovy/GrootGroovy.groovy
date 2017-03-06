package net.milosvasic.groot.languages.groovy

import org.gradle.api.Plugin
import org.gradle.api.Project


class GrootGroovy implements Plugin<Project> {

    public String version = "2.4.9"

    void apply(Project project) {
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
        project.dependencies {
            compile "junit:junit:4.12"
            testCompile "junit:junit:4.12"

            compile "org.codehaus.groovy:groovy-all:$version"
            testCompile "org.codehaus.groovy:groovy-all:$version"
        }
        project.sourceSets.main.java.srcDirs += 'src/main/java'
        project.sourceSets.main.java.srcDirs += 'build/generated-src/java'
        project.sourceSets.main.groovy.srcDirs += 'src/main/groovy'
        project.sourceSets.main.groovy.srcDirs += 'build/generated-src/groovy'
    }

}
