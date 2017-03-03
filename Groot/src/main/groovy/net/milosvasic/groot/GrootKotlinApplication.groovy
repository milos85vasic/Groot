package net.milosvasic.groot

import org.gradle.api.Plugin
import org.gradle.api.Project


class GrootKotlinApplication implements Plugin<Project> {

    @Override
    void apply(Project project) {
        SetupKotlinApplication extension = new SetupKotlinApplication(project)
        project.extensions.add("grootKotlinApplication", extension)
    }

}
