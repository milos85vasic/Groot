package net.milosvasic.groot


import org.gradle.api.Plugin
import org.gradle.api.Project


class GrootPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        Groot extension = new Groot(project)
        project.extensions.add("groot", extension)
    }

}
