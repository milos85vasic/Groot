package net.milosvasic.groot.android

import org.gradle.api.Plugin
import org.gradle.api.Project


class GrootAndroidApplication implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.repositories {
            jcenter()
            mavenCentral()
        }
        project.apply(plugin: "com.android.application")
        project.dependencies {
            compile "junit:junit:4.12"
            testCompile "junit:junit:4.12"
        }
    }

}
