package net.milosvasic.groot.deployment

import org.gradle.api.Project

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
        }
        project.assemble.finalizedBy(project.uploadArchives)
    }

}
