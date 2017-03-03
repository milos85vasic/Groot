package net.milosvasic.groot

import net.milosvasic.groot.languages.kotlin.Kotlin
import org.gradle.api.Project


class Groot {

    public Kotlin kotlin
    private Project project

    Groot(Project project) {
        this.project = project
        kotlin = new Kotlin(project)
    }

}
