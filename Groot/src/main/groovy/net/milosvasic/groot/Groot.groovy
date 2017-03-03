package net.milosvasic.groot

import groovy.util.slurpersupport.GPathResult
import net.milosvasic.groot.languages.kotlin.Kotlin
import org.gradle.api.Project


class Groot {

    public Kotlin kotlin
    private Project project
    private List<String> repos

    {
        repos = new LinkedList<>()
    }

    Groot(Project project) {
        this.project = project
        kotlin = new Kotlin(project)
    }

    void registerRepository(String repoUrl) {
        repos.add("$repoUrl")
        project.repositories {
            maven {
                url "$repoUrl"
            }
        }
    }

    void depend(String depGroup, String depName, String depVersion) {
        if ("$depVersion".endsWith("+")) {
            println(
                    String.format
                            (
                                    "We will obtain latest version for:\n\t\t[ %s ]\n\t\t[ %s ]\n\t\t[ %s ]",
                                    "$depGroup", "$depName", "$depVersion"
                            )
            )
            String latestVersion = ""
            for (repo in repos) {
                println(String.format("Contacting [ %s ]", repo))
                String rawXml
                try {
                    String group = depGroup.replace(".", "/")
                    URL url = new URL("$repo/$group/$depName/maven-metadata.xml")
                    InputStream stream = url.openStream()
                    List<String> lines = stream.readLines()
                    StringBuilder builder = new StringBuilder()
                    for (line in lines) {
                        builder.append(line)
                    }
                    rawXml = builder.toString()
                    GPathResult xml = new XmlSlurper().parseText(rawXml)
                    xml.versioning.versions.each {
                        member ->
                            member.children().each {
                                tag ->
                                    String textVersion = "$depVersion"
                                    String query = textVersion.substring(0, textVersion.length() - 2)
                                    if ("${tag.text()}".startsWith(query)) {
                                        latestVersion = "${tag.text()}"
                                    }
                            }
                    }
                } catch (Exception e) {
                    // Ignore
                }
            }
            if (latestVersion != null && latestVersion.length() > 0) {
                println(String.format("Latest version obtained [ %s ][ %s ]", depVersion, latestVersion))
                project.dependencies {
                    compile group: depGroup, name: depName, version: latestVersion
                    testCompile group: depGroup, name: depName, version: latestVersion
                }
            } else {
                println(String.format("We couldn't obtain the latest version [ %s ]", "$depVersion"))
                project.dependencies {
                    compile group: depGroup, name: depName, version: depVersion
                    testCompile group: depGroup, name: depName, version: depVersion
                }
            }
        } else {
            project.dependencies {
                compile group: depGroup, name: depName, version: depVersion
                testCompile group: depGroup, name: depName, version: depVersion
            }
        }
    }

    void setupDeployment() {
        project.apply(plugin: "maven")
        String destination
        try {
            destination = project.deploy
        } catch (Exception e) {
            // Ignore
        }
        File credentialsFile = new File(project.rootProject.projectDir, "credentials.gradle")
        if (destination != null && destination.length() > 0) {
            credentialsFile = new File(project.rootProject.projectDir, "credentials_${destination}.gradle")
        }
        if (!credentialsFile.exists()) {
            println("Credentials file does not exist. Creating default one.")
            String defaultCredentials = new StringBuilder()
                    .append("/**\n")
                    .append("* FTP server\n")
                    .append("*/\n")
                    .append("ext.ftpServer = \"repo.example.com\"\n\n")
                    .append("/**\n")
                    .append("* FTP username\n")
                    .append("*/\n")
                    .append("ext.ftpUsername = \"your_username\"\n\n")
                    .append("/**\n")
                    .append("* FTP password\n")
                    .append("*/\n")
                    .append("ext.ftpPassword = \"yout_password\"")
                    .toString()
            def srcStream = new ByteArrayInputStream(defaultCredentials.bytes)
            def dstStream = credentialsFile.newDataOutputStream()
            dstStream << srcStream
            srcStream.close()
            dstStream.close()
        } else {
            println("Credentials file exist.")
        }
        project.apply(from: credentialsFile.absolutePath)
        project.configurations {
            deployerJars
        }
        project.dependencies {
            deployerJars "org.apache.maven.wagon:wagon-ftp:2.2"
        }
        project.uploadArchives {
            repositories.mavenDeployer {
                pom.name = project.name
                pom.groupId = "${kotlin.project.projectPackage}"
                pom.version = "${kotlin.project.projectVersion}"
                pom.artifactId = project.name
                pom.packaging = "pom"
                configuration = project.configurations.deployerJars
                repository(url: "ftp://${project.ftpServer}") {
                    authentication(userName: project.ftpUsername, password: project.ftpPassword)
                }
            }
        }
        project.assemble.finalizedBy(project.uploadArchives)
    }

}
