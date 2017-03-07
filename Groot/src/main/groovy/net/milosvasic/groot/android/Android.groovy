package net.milosvasic.groot.android

import net.milosvasic.groot.languages.Language
import net.milosvasic.groot.languages.java.Java
import net.milosvasic.groot.setup.ProjectSetup
import org.gradle.api.Project


class Android implements Language {

    private Java java
    private Project mainProject
    public ProjectSetup project

    Android(Project project) {
        this.mainProject = project
        this.project = new AndroidProjectSetup(project)
        this.project.language = this
        java = new Java(project)
    }

    @Override
    String getBuildConfigClassFilename() {
        return java.buildConfigClassFilename
    }

    @Override
    String getBuildConfigClassContent(String projectPackage, String projectVersion, String projectName) {
        return java.buildConfigClassFilename
    }

    @Override
    String getMainClassName() {
        return null
    }

}
