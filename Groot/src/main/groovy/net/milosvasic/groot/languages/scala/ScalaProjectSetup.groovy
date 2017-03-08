package net.milosvasic.groot.languages.scala

import org.gradle.api.Project
import net.milosvasic.groot.setup.ProjectSetup


class ScalaProjectSetup extends ProjectSetup {

    ScalaProjectSetup(Project project) {
        super(project)
    }

    @Override
    void setup(int alpha, int beta, int version, int secondaryVersion, int tertiaryVersion, String projectGroup, String projectPackage) {
        project.dependencies {
            compile "junit:junit:4.12"
            testCompile "junit:junit:4.12"

            compile "org.scala-lang:scala-library:${project.scalaVersion}"
            testCompile "org.scala-lang:scala-library:${project.scalaVersion}"

            compile "org.scalatest:scalatest_${getVersionForTest()}:${project.scalaTestVersion}"
            testCompile "org.scalatest:scalatest_${getVersionForTest()}:${project.scalaTestVersion}"
        }
        super.setup(alpha, beta, version, secondaryVersion, tertiaryVersion, projectGroup, projectPackage)
    }

    private String getVersionForTest() {
        if (project.scalaVersion.count(".") >= 2) {
            int index = project.scalaVersion.indexOf(".")
            return project.scalaVersion.substring(0, index + 3)
        }
        return project.scalaVersion
    }

}
