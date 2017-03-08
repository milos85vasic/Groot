package net.milosvasic.groot.languages.groovy

import org.gradle.api.Project
import net.milosvasic.groot.setup.ProjectSetup

class GroovyProjectSetup extends ProjectSetup {

    GroovyProjectSetup(Project project) {
        super(project)
    }

    @Override
    void setup(int alpha, int beta, int version, int secondaryVersion, int tertiaryVersion, String projectGroup, String projectPackage) {
        project.dependencies {
            compile "junit:junit:4.12"
            testCompile "junit:junit:4.12"

            compile "org.codehaus.groovy:groovy-all:${project.groovyVersion}"
            testCompile "org.codehaus.groovy:groovy-all:${project.groovyVersion}"
        }
        super.setup(alpha, beta, version, secondaryVersion, tertiaryVersion, projectGroup, projectPackage)
    }

}
