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
        project.uploadArchives {
            repositories.mavenDeployer {
                pom.name = project.name
                pom.groupId = "$projectPackage"
                pom.version = "$projectVersion"
                pom.artifactId = project.name
                pom.packaging = "pom"
                configuration = project.configurations.deployerJars
                repository(url: "ftp://${ftp.host}") {
                    authentication(userName: ftp.username, password: ftp.password)
                }
            }
        }
        project.assemble.finalizedBy(project.uploadArchives)
    }

}
