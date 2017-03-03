package net.milosvasic.groot

import org.gradle.api.Project


class SetupKotlinProject {

    private Project project
    public String projectGroup
    public String projectPackage

    SetupKotlinProject(Project project) {
        this.project = project
    }

    void setup(String projectGroup, String projectPackage) {
        project.group = projectGroup
        this.projectGroup = projectGroup
        this.projectPackage = "${projectGroup}.$projectPackage"

        println("Setting up the project: ${this.projectPackage}")
    }

}
