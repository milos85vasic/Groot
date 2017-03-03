package net.milosvasic.groot.languages.kotlin

import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.bundling.Jar


class SetupKotlinProject {

    private Project project
    public String projectGroup
    public String projectPackage
    public String projectVersion

    SetupKotlinProject(Project project) {
        this.project = project
    }

    void setup(
            int alpha,
            int beta,
            int version,
            int secondaryVersion,
            int tertiaryVersion,
            String projectGroup,
            String projectPackage
    ) {
        project.group = projectGroup
        this.projectGroup = projectGroup
        this.projectPackage = "${projectGroup}.$projectPackage"

        println("Setting up the project: ${this.projectPackage}")
        project.apply(plugin: "maven")
        String buildVariant
        try {
            buildVariant = project.variant
        } catch (Exception e) {
            buildVariant = "DEV"
        }

        project.version = String.format("%d.%d.%d", version, secondaryVersion, tertiaryVersion)
        if (alpha > 0) {
            project.version += "_Alpha_$alpha"
        } else if (beta > 0) {
            project.version += "_Beta_$beta"
        }
        if (buildVariant != "RELEASE") {
            project.version += "_${buildVariant}"
        }
        if (buildVariant == "DEV") {
            project.version += "_${System.currentTimeMillis()}"
        }
        project.jar.archiveName = project.name + "_V" + project.version + ".jar"
        projectVersion = project.version

        StringBuilder packageStructure = new StringBuilder()
        for (String item : this.projectPackage.split("\\.")) {
            packageStructure
                    .append(item)
                    .append(File.separator)
        }
        File destination = new File(
                "${System.getProperty("user.dir")}${File.separator}${project.name}${File.separator}build${File.separator}generated-src${File.separator}kotlin${File.separator}${packageStructure.toString()}"
        )
        println("We are about to generate sources [ ${destination.absolutePath} ]")
        if (destination.exists() || destination.mkdirs()) {
            final classFile = new File(
                    "$destination${File.separator}BuildConfig.kt"
            )
            final builder = new StringBuilder("package ${this.projectPackage}")
                    .append("\n")
                    .append("\n")
                    .append("object BuildConfig {")
                    .append("\n")
                    .append("\n")
                    .append("\tval VERSION = \"${project.version}\"")
                    .append("\n")
                    .append("\tval NAME = \"${project.name}\"")
                    .append("\n")
                    .append("\n")
                    .append("}")
            classFile.write(builder.toString())
            println("We generated file [ ${classFile.absolutePath} ]")
        } else {
            println("Couldn't initialize [ ${destination.absolutePath} ]")
        }

        project.task([type: Jar, dependsOn: project.classes], "sourcesJar", {
            classifier = 'sources'
            from project.sourceSets.main.allSource
            archiveName = project.name + "_V" + project.version + "_Sources.jar"
        })

        project.task([type: Copy], "copyRelease", {
            if (buildVariant == "RELEASE") {
                from "build${File.separator}libs"
                into "Releases"
            } else {
                println("We will not copy release jar for this build.")
            }
        })

        project.assemble.finalizedBy(project.sourcesJar)
        project.sourcesJar.finalizedBy(project.copyRelease)
    }

}
