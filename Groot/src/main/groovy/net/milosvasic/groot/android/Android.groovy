package net.milosvasic.groot.android

import net.milosvasic.groot.languages.Language
import net.milosvasic.groot.languages.java.Java
import net.milosvasic.groot.setup.ApplicationSetup
import net.milosvasic.groot.setup.ProjectSetup
import org.gradle.api.Project


class Android implements Language {

    private Java java
    private Project mainProject
    public ProjectSetup project
    public ApplicationSetup application

    Android(Project project) {
        this.mainProject = project
        this.project = new ProjectSetup(project)
        this.project.language = this
        application = new ApplicationSetup(project)
        application.language = this
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
