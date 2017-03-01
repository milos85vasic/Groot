package net.milosvasic.gradle

import org.gradle.api.Project
import org.gradle.api.Plugin

class Groot implements Plugin<Project> {
    void apply(Project target) {
        println("Groot helo!")
    }
}
