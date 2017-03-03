package net.milosvasic.groot.languages.kotlin

import net.milosvasic.groot.languages.Language
import org.gradle.api.Project

class Kotlin implements Language {

    private Project mainProject
    public SetupKotlinProject project
    public SetupKotlinApplication application

    Kotlin(Project project) {
        this.mainProject = project
        this.project = new SetupKotlinProject(project)
        application = new SetupKotlinApplication(project)
    }

}
