package net.milosvasic.groot.setup

import net.milosvasic.groot.languages.Language
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.bundling.Jar


class ProjectSetup {

    public Language language
    protected Project project
    public String projectGroup
    public String projectPackage
    public String projectVersion
    public String projectBuildVariant
    public boolean isProjectSetup = false
    protected boolean generateBuildConfig = true

    ProjectSetup(Project project) {
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
        projectBuildVariant = buildVariant

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

        if (project.hasProperty("jar")) {
            project.jar.archiveName = project.name + "_V" + project.version + ".jar"
        }
        projectVersion = project.version

        if (generateBuildConfig) {
            StringBuilder packageStructure = new StringBuilder()
            for (String item : this.projectPackage.split("\\.")) {
                packageStructure
                        .append(item)
                        .append(File.separator)
            }
            String langName = language.class.simpleName.toLowerCase().replace(" ", "_")
            File destination = new File(
                    "${System.getProperty("user.dir")}${File.separator}${project.name}${File.separator}build${File.separator}generated-src${File.separator}$langName${File.separator}${packageStructure.toString()}"
            )
            println("We are about to generate sources [ ${destination.absolutePath} ]")
            if (destination.exists() || destination.mkdirs()) {
                final classFile = new File(
                        "$destination${File.separator}${language.buildConfigClassFilename}"
                )
                String content = language.getBuildConfigClassContent(
                        this.projectPackage,
                        project.version as String,
                        project.name,
                        buildVariant
                )
                classFile.write(content)
                println("We generated file [ ${classFile.absolutePath} ]")
            } else {
                println("Couldn't initialize [ ${destination.absolutePath} ]")
            }
        }

        project.task([type: Copy], "copyRelease", {
            if (buildVariant == "RELEASE") {
                from "build${File.separator}libs"
                into "Releases"
            } else {
                println("We will not copy release archive for this build.")
            }
        })

        if (project.hasProperty("android")) {
            String filetype = "aar"
            if (project.android.hasProperty("applicationVariants")) {
                filetype = "apk"
            }
            if (project.android.productFlavors.size() > 0) {
                project.android.productFlavors.each {
                    flavor ->
                        project.android.buildTypes.each {
                            buildType ->
                                String capitalized = "${buildType.name}".substring(0, 1).toUpperCase()
                                capitalized += "${buildType.name}".substring(1, "${buildType.name}".length())
                                String variantName = "${flavor.name}$capitalized"
                                setupAndroidJarSourcesArtifact(variantName, projectPackage, projectVersion)
                        }
                }
            } else {
                project.android.buildTypes.each {
                    buildType ->
                        String variantName = "${buildType.name}"
                        setupAndroidJarSourcesArtifact(variantName, projectPackage, projectVersion)
                }
            }
        } else {
            setupJarSourcesArtifact()
        }

        project.assemble.finalizedBy(project.copyRelease)
        isProjectSetup = true
    }

    private void setupJarSourcesArtifact() {
        if (project.hasProperty("classes")) {
            String jarSourcesName = "sourcesJar"
            Task sourcesTask = project.task([type: Jar, dependsOn: project.classes], jarSourcesName, {
                classifier = 'sources'
                from project.sourceSets.main.allSource
                archiveName = project.name + "_V" + project.version + "_Sources.jar"
            })
            project.artifacts {
                archives(sourcesTask) {
                    name jarSourcesName
                }
            }
        }
    }

    private void setupAndroidJarSourcesArtifact(String variantName, String projectPackage, String projectVersion) {
        if (project.android.hasProperty("libraryVariants")) {
            String jarSourcesName = "${variantName}JarSources"
            Task sourcesTask = project.task([type: Jar], jarSourcesName, {
                from project.android.sourceSets.main.java.srcDirs
                classifier = 'sources'
            })
            project.artifacts {
                archives(sourcesTask) {
                    name jarSourcesName
                }
            }
        }
    }

}
