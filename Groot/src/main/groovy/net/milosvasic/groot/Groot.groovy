package net.milosvasic.groot

import groovy.util.slurpersupport.GPathResult
import net.milosvasic.groot.android.Android
import net.milosvasic.groot.deployment.Deploy
import net.milosvasic.groot.languages.groovy.Groovy
import net.milosvasic.groot.languages.java.Java
import net.milosvasic.groot.languages.kotlin.Kotlin
import net.milosvasic.groot.languages.scala.Scala
import org.gradle.api.Project


class Groot {

    public Java java
    public Scala scala
    public Groovy groovy
    public Kotlin kotlin
    public Android android
    private Project project
    public Deploy deployment
    private List<String> repos

    {
        repos = new LinkedList<>()
    }

    Groot(Project project) {
        this.project = project
        java = new Java(project)
        scala = new Scala(project)
        kotlin = new Kotlin(project)
        groovy = new Groovy(project)
        android = new Android(project)
        deployment = new Deploy(project)
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
        String archiveExtension = ""
        if (project.hasProperty("android")) {
            archiveExtension = "@aar"
        }
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
                println(String.format("[ RESOLUTION ] Latest version obtained [ %s ][ %s ]", depVersion, latestVersion))
                project.dependencies {
                    compile "$depGroup:$depName:$latestVersion$archiveExtension"
                    testCompile "$depGroup:$depName:$latestVersion$archiveExtension"

                    compile "$depGroup:$depName:$latestVersion:sources"
                    testCompile "$depGroup:$depName:$latestVersion:sources"
                }
            } else {
                println(String.format("[ RESOLUTION ] We couldn't obtain the latest version [ %s ]", "$depVersion"))
                project.dependencies {
                    compile "$depGroup:$depName:$depVersion$archiveExtension"
                    testCompile "$depGroup:$depName:$depVersion$archiveExtension"

                    compile "$depGroup:$depName:$depVersion:sources"
                    testCompile "$depGroup:$depName:$depVersion:sources"
                }
            }
        } else {
            project.dependencies {
                compile "$depGroup:$depName:$depVersion$archiveExtension"
                testCompile "$depGroup:$depName:$depVersion$archiveExtension"

                compile "$depGroup:$depName:$depVersion:sources"
                testCompile "$depGroup:$depName:$depVersion:sources"
            }
        }
    }

}
