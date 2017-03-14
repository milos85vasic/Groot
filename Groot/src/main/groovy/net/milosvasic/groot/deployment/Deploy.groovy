package net.milosvasic.groot.deployment

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.bundling.Jar

class Deploy {

    public Ftp ftp
    private Project project

    Deploy(Project project) {
        this.project = project
        ftp = new Ftp()
    }

    void setup(String projectPackage, String projectVersion) {
        project.apply(plugin: "maven")
        String destination
        try {
            destination = project.deploy
        } catch (Exception e) {
            // Ignore
        }
        project.configurations {
            deployerJars
        }
        project.dependencies {
            deployerJars "org.apache.maven.wagon:wagon-ftp:2.2"
        }
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
                                String artifactName = "${project.name}_${projectVersion}"
                                String fileName = new StringBuilder()
                                        .append(project.buildDir)
                                        .append(File.separator)
                                        .append("outputs")
                                        .append(File.separator)
                                        .append(filetype)
                                        .append(File.separator)
                                        .append("$variantName")
                                        .toString()
                                File artifactFile = new File(fileName, "${artifactName}.$filetype")
                                project.artifacts {
                                    archives(artifactFile) {
                                        name variantName
                                    }
                                }
                                project.uploadArchives {
                                    repositories {
                                        mavenDeployer {
                                            repository(url: "ftp://${ftp.host}") {
                                                authentication(
                                                        userName: ftp.username,
                                                        password: ftp.password
                                                )
                                            }
                                            configuration = project.configurations.deployerJars
                                            addFilter(variantName) {
                                                artifact, file ->
                                                    artifact.name == variantName
                                            }
                                            pom(variantName).name = project.name
                                            pom(variantName).groupId = projectPackage
                                            pom(variantName).version = "${variantName}_$projectVersion"
                                            pom(variantName).artifactId = project.name
                                            pom(variantName).packaging = filetype
                                        }
                                    }
                                }
                                setupAndroidJarSourcesArtifact(variantName, projectPackage, projectVersion)
                        }
                }
            } else {
                project.android.buildTypes.each {
                    buildType ->
                        String variantName = "${buildType.name}"
                        String artifactName = "${project.name}_${projectVersion}"
                        String fileName = new StringBuilder()
                                .append(project.buildDir)
                                .append(File.separator)
                                .append("outputs")
                                .append(File.separator)
                                .append(filetype)
                                .append(File.separator)
                                .append("$variantName")
                                .toString()
                        File artifactFile = new File(fileName, "${artifactName}.$filetype")
                        project.artifacts {
                            archives(artifactFile) {
                                name variantName
                            }
                        }
                        project.uploadArchives {
                            repositories {
                                mavenDeployer {
                                    repository(url: "ftp://${ftp.host}") {
                                        authentication(
                                                userName: ftp.username,
                                                password: ftp.password
                                        )
                                    }
                                    configuration = project.configurations.deployerJars
                                    addFilter(variantName) {
                                        artifact, file ->
                                            artifact.name == variantName
                                    }
                                    pom(variantName).name = project.name
                                    pom(variantName).groupId = projectPackage
                                    pom(variantName).version = "${variantName}_$projectVersion"
                                    pom(variantName).artifactId = project.name
                                    pom(variantName).packaging = filetype
                                }
                            }
                        }
                        setupAndroidJarSourcesArtifact(variantName, projectPackage, projectVersion)
                }
            }
        } else {
            project.uploadArchives {
                repositories {
                    mavenDeployer {
                        configuration = project.configurations.deployerJars
                        repository(url: "ftp://${ftp.host}") {
                            authentication(
                                    userName: ftp.username,
                                    password: ftp.password
                            )
                        }
                        pom.name = project.name
                        pom.groupId = projectPackage
                        pom.version = projectVersion
                        pom.artifactId = project.name
                        pom.packaging = "jar"
                    }
                }
            }
            setupJarSourcesArtifact(projectPackage, projectVersion)
        }
        project.assemble.finalizedBy(project.uploadArchives)
    }

    private void setupJarSourcesArtifact(String projectPackage, String projectVersion) {
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
            project.uploadArchives {
                repositories {
                    mavenDeployer {
                        repository(url: "ftp://${ftp.host}") {
                            authentication(
                                    userName: ftp.username,
                                    password: ftp.password
                            )
                        }
                        configuration = project.configurations.deployerJars
                        addFilter(jarSourcesName) {
                            artifact, file ->
                                artifact.name == jarSourcesName
                        }
                        pom(jarSourcesName).name = project.name
                        pom(jarSourcesName).groupId = projectPackage
                        pom(jarSourcesName).version = projectVersion
                        pom(jarSourcesName).artifactId = project.name
                        pom(jarSourcesName).packaging = "jar"
                    }
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
            project.uploadArchives {
                repositories {
                    mavenDeployer {
                        repository(url: "ftp://${ftp.host}") {
                            authentication(
                                    userName: ftp.username,
                                    password: ftp.password
                            )
                        }
                        configuration = project.configurations.deployerJars
                        addFilter(jarSourcesName) {
                            artifact, file ->
                                artifact.name == jarSourcesName
                        }
                        pom(jarSourcesName).name = project.name
                        pom(jarSourcesName).groupId = projectPackage
                        pom(jarSourcesName).version = "${variantName}_$projectVersion"
                        pom(jarSourcesName).artifactId = project.name
                        pom(jarSourcesName).packaging = "jar"
                    }
                }
            }
        }
    }

}
