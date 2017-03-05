package net.milosvasic.groot.setup

import net.milosvasic.groot.languages.Language
import org.gradle.api.Project


class ApplicationSetup {

    private Project project
    public Language language

    ApplicationSetup(Project project) {
        this.project = project
    }

    void setup(String versionPackage) {
        setup(versionPackage, language.mainClassName)
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
