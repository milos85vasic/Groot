package net.milosvasic.groot.languages.kotlin

import net.milosvasic.groot.languages.Language
import net.milosvasic.groot.setup.ApplicationSetup
import net.milosvasic.groot.setup.ProjectSetup
import org.gradle.api.Project

class Kotlin implements Language {

    private Project mainProject
    public KotlinProjectSetup project
    public ApplicationSetup application

    Kotlin(Project project) {
        this.mainProject = project
        this.project = new KotlinProjectSetup(project)
        this.project.language = this
        application = new ApplicationSetup(project)
        application.language = this
    }

    @Override
    String getBuildConfigClassFilename() {
        return "BuildConfig.kt"
    }

    @Override
    String getBuildConfigClassContent(String projectPackage, String projectVersion, String projectName, String buildVariant) {
        return new StringBuilder("package $projectPackage")
                .append("\n")
                .append("\n")
                .append("object BuildConfig {")
                .append("\n")
                .append("\n")
                .append("\tval VERSION = \"$projectVersion\"")
                .append("\n")
                .append("\tval NAME = \"$projectName\"")
                .append("\n")
                .append("\tval VARIANT = \"$buildVariant\"")
                .append("\n")
                .append("\n")
                .append("}")
                .toString()
    }

    @Override
    String getMainClassName() {
        return "MainKt"
    }

    void setVersion(String kotlinVersion){
        mainProject.kotlinVersion = kotlinVersion
    }

    String getVersion() {
        return mainProject.kotlinVersion
    }

}
