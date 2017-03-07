package net.milosvasic.groot.android

import org.gradle.api.Plugin
import org.gradle.api.Project


class GrootAndroidLibrary implements Plugin<Project> {

    public String gradleVersion = "2.3.0"

    @Override
    void apply(Project project) {
        project.buildscript {
            repositories {
                jcenter()
                mavenCentral()
            }
            dependencies {
                classpath "com.android.tools.build:gradle:$gradleVersion"
            }
        }
        project.apply(plugin: "com.android.library")

//        project.apply(plugin: "java")
//        project.buildscript {
//            repositories {
//                jcenter()
//                mavenCentral()
//            }
//        }
//        project.repositories {
//            jcenter()
//            mavenCentral()
//        }
//        project.dependencies {
//            compile "junit:junit:4.12"
//            testCompile "junit:junit:4.12"
//        }
//        project.sourceSets.main.java.srcDirs += 'src/main/java'
//        project.sourceSets.main.java.srcDirs += 'build/generated-src/java'
//        project.sourceSets.test.java.srcDirs += 'src/test/java'
    }

}
