package net.milosvasic.groot.languages.java

import net.milosvasic.groot.languages.Language
import net.milosvasic.groot.setup.ProjectSetup
import net.milosvasic.groot.setup.ApplicationSetup
import org.gradle.api.Project

class Java implements Language {

    private Project mainProject
    public ProjectSetup project
    public ApplicationSetup application

    Java(Project project) {
        this.mainProject = project
        this.project = new ProjectSetup(project)
        this.project.language = this
        application = new ApplicationSetup(project)
        application.language = this
    }

    @Override
    String getBuildConfigClassFilename() {
        return "BuildConfig.java"
    }

    @Override
    String getBuildConfigClassContent(String projectPackage, String projectVersion, String projectName) {
        return new StringBuilder("package $projectPackage;")
                .append("\n")
                .append("\n")
                .append("class BuildConfig {")
                .append("\n")
                .append("\n")
                .append("\tpublic static String VERSION = \"$projectVersion\";")
                .append("\n")
                .append("\tpublic static String NAME = \"$projectName\";")
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
