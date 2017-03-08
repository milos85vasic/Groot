package net.milosvasic.groot.languages.kotlin

import org.gradle.api.Project
import net.milosvasic.groot.setup.ProjectSetup



class KotlinProjectSetup extends ProjectSetup {

    KotlinProjectSetup(Project project) {
        super(project)
    }

    @Override
    void setup(int alpha, int beta, int version, int secondaryVersion, int tertiaryVersion, String projectGroup, String projectPackage) {
        project.dependencies {
            compile "junit:junit:4.12"
            testCompile "junit:junit:4.12"

            compile "org.jetbrains.kotlin:kotlin-stdlib:${project.kotlinVersion}"
            compile "org.jetbrains.kotlin:kotlin-reflect:${project.kotlinVersion}"

            testCompile "org.jetbrains.kotlin:kotlin-test:${project.kotlinVersion}"
            testCompile "org.jetbrains.kotlin:kotlin-test-junit:${project.kotlinVersion}"
        }
        super.setup(alpha, beta, version, secondaryVersion, tertiaryVersion, projectGroup, projectPackage)
    }

}
