package net.milosvasic.groot.languages.scala

import net.milosvasic.groot.languages.Language
import net.milosvasic.groot.setup.ApplicationSetup
import net.milosvasic.groot.setup.ProjectSetup
import org.gradle.api.Project


class Scala implements Language {

    private Project mainProject
    public ProjectSetup project
    public ApplicationSetup application

    Scala(Project project) {
        this.mainProject = project
        this.project = new ProjectSetup(project)
        this.project.language = this
        application = new ApplicationSetup(project)
        application.language = this
    }

    @Override
    String getBuildConfigClassFilename() {
        return "BuildConfig.scala"
    }

    @Override
    String getBuildConfigClassContent(String projectPackage, String projectVersion, String projectName) {
        return new StringBuilder("package $projectPackage")
                .append("\n")
                .append("\n")
                .append("class BuildConfig {")
                .append("\n")
                .append("\n")
                .append("\tval VERSION = \"$projectVersion\"")
                .append("\n")
                .append("\tval NAME = \"$projectName\"")
                .append("\n")
                .append("\n")
                .append("}")
                .toString()
    }

    @Override
    String getMainClassName() {
        return "Main"
    }

}
