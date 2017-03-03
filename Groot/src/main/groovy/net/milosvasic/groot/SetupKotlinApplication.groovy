package net.milosvasic.groot

import org.gradle.api.Project


class SetupKotlinApplication {
    private Project project

    SetupKotlinApplication(Project project) {
        this.project = project
    }

    void setup(String versionPackage) {
        setup(versionPackage, "MainKt")
    }

    void setup(String versionPackage, String mainClass) {
        project.apply(plugin: "application")
        project.mainClassName = "${versionPackage}.$mainClass"
        println("mainClassName: ${project.mainClassName}")
        project.jar {
            manifest.attributes "Main-Class": "${project.mainClassName}"
            from {
                project.configurations.compile.collect { it.isDirectory() ? it : project.zipTree(it) }
            }
        }
    }
}
