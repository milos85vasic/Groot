package net.milosvasic.groot.android

import org.gradle.api.Plugin
import org.gradle.api.Project


class GrootAndroidLibrary implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.repositories {
            jcenter()
            mavenCentral()
        }
        project.apply(plugin: "com.android.library")
        project.dependencies {
            compile "junit:junit:4.12"
            testCompile "junit:junit:4.12"
        }
        project.sourceSets.main.java.srcDirs += 'src/main/java'
        project.sourceSets.main.java.srcDirs += 'build/generated-src/java'
        project.sourceSets.test.java.srcDirs += 'src/test/java'
    }

}
