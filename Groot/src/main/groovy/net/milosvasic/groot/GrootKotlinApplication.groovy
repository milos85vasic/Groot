package net.milosvasic.groot

import org.gradle.api.Plugin
import org.gradle.api.Project


class GrootKotlinApplication implements Plugin<Project> {

    @Override
    void apply(Project project) {
        SetupKotlinApplication extensions = new SetupKotlinApplication(project)
        project.extensions.add("application", extensions)
    }

    class SetupKotlinApplication {
        private Project project

        SetupKotlinApplication(Project project) {
            this.project = project
        }

        void setup(String versionPackage) {
            println(">>>> " + versionPackage)
        }
    }

}
