package net.milosvasic.groot

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

}
